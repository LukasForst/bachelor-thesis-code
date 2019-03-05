package pw.forst.olb.hub.providers

import pw.forst.olb.common.dto.job.JobData
import java.io.File

/**
 * Converts given data into File representation for copying data into docker container
 * */
interface InputFileProvider {

    /**
     * Takes [JobData] and returns Folder file descriptor where [JobData.jobInput] is stored
     * */
    fun createFile(data: JobData): File
}
