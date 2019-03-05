package pw.forst.olb.hub.providers

import pw.forst.olb.common.dto.job.JobData
import java.io.File

interface InputFileProvider {
    fun createFile(data: JobData): File
}
