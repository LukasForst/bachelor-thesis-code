package pw.forst.olb.hub

import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.junit.jupiter.api.Test
import pw.forst.olb.common.dto.ResourcesLimit
import pw.forst.olb.common.dto.docker.DockerCommandTask
import pw.forst.olb.common.dto.docker.DockerCommandTaskResult
import pw.forst.olb.common.dto.docker.DockerContainerCreateResult
import pw.forst.olb.common.dto.docker.DockerContainerCreateTask
import pw.forst.olb.common.dto.docker.DockerContainerInfo
import pw.forst.olb.common.dto.docker.DockerHost
import pw.forst.olb.common.dto.docker.DockerImage
import pw.forst.olb.common.dto.docker.DockerJobSendDataCommand
import pw.forst.olb.common.dto.docker.DockerJobStopTask
import pw.forst.olb.common.dto.docker.DockerJobStopTaskResult
import pw.forst.olb.common.dto.docker.DockerResourcesChangeTask
import pw.forst.olb.common.dto.docker.DockerResourcesChangeTaskResult
import pw.forst.olb.common.dto.docker.DockerTask
import pw.forst.olb.common.dto.job.JobData
import pw.forst.olb.common.dto.job.JobId
import pw.forst.olb.common.dto.job.JobInput
import pw.forst.olb.hub.client.DefaultDockerClientProvider
import pw.forst.olb.hub.client.EagerReleaseStrategy
import pw.forst.olb.hub.error.DockerTasksErrorHandling
import pw.forst.olb.hub.execution.DockerTaskExecutor
import pw.forst.olb.hub.providers.SimpleInputFileProvider

internal class IntegrationTest {

    private companion object : KLogging()


    //    @Disabled("This is docker integration test! Run only on environment with docker!")
    @Test
    fun `integration test - create, update, shutdown`() = runBlocking {
        val dockerClientProvider = DefaultDockerClientProvider(EagerReleaseStrategy(), coroutineContext)
        val errorHandler = DockerTasksErrorHandling<DockerTask>()
        val inputFileProvider = SimpleInputFileProvider()

        val dockerTaskExecutor = DockerTaskExecutor(dockerClientProvider, errorHandler, inputFileProvider, coroutineContext)

        val jobId = JobId(1L)
        val dockerHost = DockerHost("unix:///var/run/docker.sock") //localhost

        val createTask = DockerContainerCreateTask(
            jobId,
            dockerHost,
            ResourcesLimit(2, 512),
            DockerImage("testing", "latest"),
            JobData(jobId, JobInput("Helo World!", "my_file.txt", "/app")),
            "pioneer"
        )
        val resultCreate = dockerTaskExecutor.executeSuspended(createTask) as DockerContainerCreateResult

        logger.info { resultCreate }

        val containerInfo = DockerContainerInfo(resultCreate.containerId, dockerHost)
        val updateTask = DockerResourcesChangeTask(
            jobId,
            containerInfo,
            ResourcesLimit(3, 1024)
        )

        val resultUpdate = dockerTaskExecutor.executeSuspended(updateTask) as DockerResourcesChangeTaskResult
        logger.info { resultUpdate }

        val commandTask = DockerCommandTask(
            jobId,
            containerInfo,
            DockerJobSendDataCommand(
                arrayOf("touch /app/i_said_hello!.txt ; echo Hello World!!!!!!!")
            )
        )
        val resultCommand = dockerTaskExecutor.executeSuspended(commandTask) as DockerCommandTaskResult
        logger.info { resultCommand }

        val closeTask = DockerJobStopTask(
            jobId,
            containerInfo
        )

        val resultClose = dockerTaskExecutor.executeSuspended(closeTask) as DockerJobStopTaskResult

        logger.info { resultClose }
    }
}
