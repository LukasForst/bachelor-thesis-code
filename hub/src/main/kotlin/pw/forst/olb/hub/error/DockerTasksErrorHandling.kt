package pw.forst.olb.hub.error

import mu.KLogging
import pw.forst.olb.common.dto.docker.DockerTask

class DockerTasksErrorHandling<T> : TasksErrorHandling<T> where T : DockerTask {
    private companion object : KLogging()

    override fun handle(task: T, ex: Exception) {
        logger.error(ex) { "While executing $task something bad happen" }
    }

    override fun handle(task: T, ex: Throwable) {
        logger.error(ex) { "While executing $task something bad happen" }
    }
}
