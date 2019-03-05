package pw.forst.olb.hub.execution

import pw.forst.olb.common.dto.docker.DockerTask
import pw.forst.olb.hub.api.DockerTasksQueue
import java.util.concurrent.TimeUnit

class DockerTasksConsumer(
    private val queue: DockerTasksQueue,
    private val taskExecutor: DockerTaskExecutor,
    private val stoppingCheckInterval: Pair<Long, TimeUnit> = 2L to TimeUnit.SECONDS
) : Runnable {

    private var shouldRun = true

    fun stopGracefully() {
        shouldRun = false
    }

    override fun run() {
        while (shouldRun) {
            handleTask(queue.poll(stoppingCheckInterval.first, stoppingCheckInterval.second))
        }
    }

    private fun handleTask(task: DockerTask) = taskExecutor.executeAsync(task)
}
