package pw.forst.olb.hub

import kotlinx.coroutines.runBlocking
import mu.KLogging
import pw.forst.olb.common.dto.ResourcesLimit
import pw.forst.olb.common.dto.docker.DockerContainerCreateTask
import pw.forst.olb.common.dto.docker.DockerHost
import pw.forst.olb.common.dto.docker.DockerImage
import pw.forst.olb.common.dto.docker.DockerTask
import pw.forst.olb.common.dto.job.JobData
import pw.forst.olb.common.dto.job.JobId
import pw.forst.olb.common.dto.job.JobInput
import pw.forst.olb.hub.client.DefaultDockerClientProvider
import pw.forst.olb.hub.client.EagerReleaseStrategy
import pw.forst.olb.hub.error.DockerTasksErrorHandling
import pw.forst.olb.hub.execution.DockerTaskExecutor
import pw.forst.olb.hub.providers.SimpleInputFileProvider


object Main : KLogging() {

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val dockerClientProvider = DefaultDockerClientProvider(EagerReleaseStrategy(), coroutineContext)
        val errorHandler = DockerTasksErrorHandling<DockerTask>()
        val inputFileProvider = SimpleInputFileProvider()

        val dockerTaskExecutor = DockerTaskExecutor(dockerClientProvider, errorHandler, inputFileProvider, coroutineContext)

        val jobId = JobId(1L)
        val createTask = DockerContainerCreateTask(
            jobId,
            DockerHost("localhost"),
            ResourcesLimit(2, 512),
            DockerImage("testing", "latest"),
            JobData(jobId, JobInput("Helo World!", "my_file.txt", "/app")),
            "pioneer"
        )
        dockerTaskExecutor.executeAsync(createTask).join()
    }
}
