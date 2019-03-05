package pw.forst.olb.hub.execution

import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.HostConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mu.KLogging
import pw.forst.olb.common.dto.ResourcesLimit
import pw.forst.olb.common.dto.docker.DockerCommandTask
import pw.forst.olb.common.dto.docker.DockerCommandTaskResult
import pw.forst.olb.common.dto.docker.DockerContainerCreateResult
import pw.forst.olb.common.dto.docker.DockerContainerCreateTask
import pw.forst.olb.common.dto.docker.DockerFailureTaskResult
import pw.forst.olb.common.dto.docker.DockerHost
import pw.forst.olb.common.dto.docker.DockerJobSendDataCommand
import pw.forst.olb.common.dto.docker.DockerJobStopTask
import pw.forst.olb.common.dto.docker.DockerJobStopTaskResult
import pw.forst.olb.common.dto.docker.DockerResourcesChangeTask
import pw.forst.olb.common.dto.docker.DockerResourcesChangeTaskResult
import pw.forst.olb.common.dto.docker.DockerTask
import pw.forst.olb.common.dto.docker.DockerTaskResult
import pw.forst.olb.common.extensions.mapToSet
import pw.forst.olb.hub.client.DockerClientProvider
import pw.forst.olb.hub.error.ContainerNotCreatedException
import pw.forst.olb.hub.error.DockerHostNotReachedException
import pw.forst.olb.hub.error.TasksErrorHandling
import pw.forst.olb.hub.providers.InputFileProvider
import kotlin.coroutines.CoroutineContext

@Suppress("MemberVisibilityCanBePrivate") //used for testing
class DockerTaskExecutor(
    private val clientProvider: DockerClientProvider,
    private val errorHandler: TasksErrorHandling<DockerTask>,
    private val inputFileProvider: InputFileProvider,
    override val coroutineContext: CoroutineContext
) : DockerContext {

    private companion object : KLogging() {
        const val secondsBeforeKillingContainer = 100
    }

    private suspend fun obtainClient(host: DockerHost) = withContext(Dispatchers.IO) {
        clientProvider.getClient(host)
    } ?: throw DockerHostNotReachedException("Docker host $host not responding!")

    private fun buildHostConfig(resourcesLimit: ResourcesLimit): HostConfig = HostConfig.builder()
        .cpusetCpus("0-${resourcesLimit.numberOfCores}")
        .memory(resourcesLimit.memoryInMb * 1024 * 1024) //TODO find out in which unit is memory in spotify client, assuming in bytes
        .build()


    /**
     * Executes given docker task in the coroutine context, returns coroutine job
     * */
    fun executeAsync(task: DockerTask) = launch { executeSuspended(task) }

    /**
     * Executes given docker task in coroutine context as suspended method
     * */
    suspend fun executeSuspended(task: DockerTask): DockerTaskResult =
        runCatching {
            obtainClient(task.dockerHost)
                .let { client ->
                    withContext(Dispatchers.IO) {
                        when (task) {
                            is DockerResourcesChangeTask -> execute(task, client)
                            is DockerJobStopTask -> execute(task, client)
                            is DockerCommandTask -> execute(task, client)
                            is DockerContainerCreateTask -> execute(task, client)
                        }
                    }
                }
        }
            .also { clientProvider.returnClient(task.dockerHost) }
            .getOrElse { throwAble ->
                logger.error(throwAble) { "Exception while updating resources configuration occurred!" }
                DockerFailureTaskResult(task, throwAble).also { errorHandler.handle(task, throwAble) }
            }

    fun execute(task: DockerContainerCreateTask, client: DockerClient): DockerContainerCreateResult {
        val containerId = ContainerConfig.builder()
            .image("${task.image.name}:${task.image.tag}") //TODO another parameters?
            .hostConfig(buildHostConfig(task.resourcesLimit))
            .build()
            .let {
                with(client) {
                    logger.info { "Creating new container for job id ${task.jobId}" }
                    removeIfExist(task.containerName) //TODO remove this
                    createContainer(it, task.containerName).also { logger.info { "Container created! - Id ${it.id()}" } }
                }
            }
            ?.id() ?: throw ContainerNotCreatedException("For some reason it was not possible to crete docker container for job id ${task.jobId}")

        with(client) {
            logger.info { "Copying data to the container - $containerId" }
            copyToContainer(inputFileProvider.createFile(task.dataToInsert).toPath(), containerId, task.dataToInsert.jobInput.path)
                .also { logger.info { "Data copied - $containerId" } }

            logger.info { "Starting container - $containerId" }
            startContainer(containerId).also { logger.info { "Container started - $containerId" } }
        }
        return DockerContainerCreateResult(containerId, task)
    }


    fun execute(task: DockerResourcesChangeTask, client: DockerClient): DockerResourcesChangeTaskResult {
        client
            .also { logger.info { "Updating resource configuration for ${task.dockerContainer}" } }
            .updateContainer(task.dockerContainer.containerId, buildHostConfig(task.resourcesLimit))
            ?.warnings()?.joinToString { " ; " }
            ?.let { logger.warn { "Executed with following warnings: $it" } }
            ?: logger.info { "Execution successful!" }
        return DockerResourcesChangeTaskResult(task)
    }


    fun execute(task: DockerJobStopTask, client: DockerClient): DockerJobStopTaskResult {
        client
            .also { logger.info { "Stopping container ${task.dockerContainer}" } }
            .stopContainer(task.dockerContainer.containerId, secondsBeforeKillingContainer)
            .also { logger.info { "Container ${task.dockerContainer.containerId} stopped!" } }
        return DockerJobStopTaskResult(task)
    }

    fun execute(task: DockerCommandTask, client: DockerClient): DockerCommandTaskResult = task.command.let {
        when (it) {
            is DockerJobSendDataCommand -> executeCommand(client, task.dockerContainer.containerId, it.cmds)
        }
        return DockerCommandTaskResult(task)
    }

    private fun executeCommand(client: DockerClient, containerId: String, cmd: Array<String>) = client
        .also { logger.info { "Executing command $cmd for container $containerId" } }
        .execCreate(containerId, cmd).id()
        .also { logger.info { "New command created - container: $containerId, command id: $it" } }
        .let { commandId -> client.execStart(commandId).use { logger.info { it.readFully() } } } //TODO verify whether this ends successfully
        .also { logger.info { "Command successfully executed!" } }

}

fun DockerClient.removeIfExist(containerName: String) {
    val containers = this.listContainers(DockerClient.ListContainersParam.allContainers())
        .filter { it.names()?.contains("/$containerName") ?: false }
    val ids = containers.mapToSet { it.id() }
    ids.forEach { this.removeContainer(it) }
}
