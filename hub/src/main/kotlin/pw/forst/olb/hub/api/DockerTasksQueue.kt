package pw.forst.olb.hub.api

import pw.forst.olb.common.dto.docker.DockerTask
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue

class DockerTasksQueue(
    private val queue: BlockingQueue<DockerTask> = PriorityBlockingQueue<DockerTask>()
) : BlockingQueue<DockerTask> by queue


