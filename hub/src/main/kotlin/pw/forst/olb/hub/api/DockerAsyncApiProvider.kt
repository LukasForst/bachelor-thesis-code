package pw.forst.olb.hub.api

import pw.forst.olb.common.dto.docker.DockerTask

/**
 * Default implementation of [DockerAsyncTaskExecutionApi]
 * */
class DockerAsyncApiProvider(private val queue: DockerTasksQueue) : DockerAsyncTaskExecutionApi {

    override fun insertTask(task: DockerTask) {
        queue.add(task)
    }

}
