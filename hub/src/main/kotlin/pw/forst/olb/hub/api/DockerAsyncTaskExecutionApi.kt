package pw.forst.olb.hub.api

import pw.forst.olb.common.dto.docker.DockerTask


/**
 * Provides access to the blocking queue where all requests should be stored
 * */
interface DockerAsyncTaskExecutionApi {


    /**
     * Adds task to execution
     * */
    fun insertTask(task: DockerTask)
}
