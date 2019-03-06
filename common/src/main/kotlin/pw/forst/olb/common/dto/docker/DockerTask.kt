package pw.forst.olb.common.dto.docker

import pw.forst.olb.common.dto.ResourcesLimit
import pw.forst.olb.common.dto.job.JobData
import pw.forst.olb.common.dto.job.JobId


sealed class DockerTask : Comparable<DockerTask> {
    abstract val jobId: JobId
    abstract val priority: Long
    abstract val dockerHost: DockerHost
    override fun compareTo(other: DockerTask): Int = priority.compareTo(other.priority)
}

data class DockerResourcesChangeTask(
    override val jobId: JobId,
    val dockerContainer: DockerContainerInfo,
    val resourcesLimit: ResourcesLimit,
    override val priority: Long = 0
) : DockerTask() {
    override val dockerHost: DockerHost
        get() = dockerContainer.dockerHost
}

data class DockerJobStopTask(
    override val jobId: JobId,
    val dockerContainer: DockerContainerInfo,
    override val priority: Long = 0
) : DockerTask() {
    override val dockerHost: DockerHost
        get() = dockerContainer.dockerHost
}

data class DockerCommandTask(
    override val jobId: JobId,
    val dockerContainer: DockerContainerInfo,
    val command: DockerCommand,
    override val priority: Long = 0
) : DockerTask() {
    override val dockerHost: DockerHost
        get() = dockerContainer.dockerHost
}

data class DockerContainerCreateTask(
    override val jobId: JobId,
    override val dockerHost: DockerHost,
    val resourcesLimit: ResourcesLimit,
    val image: DockerImage,
    val dataToInsert: JobData,
    val containerName: String,
    override val priority: Long = 0
) : DockerTask()
