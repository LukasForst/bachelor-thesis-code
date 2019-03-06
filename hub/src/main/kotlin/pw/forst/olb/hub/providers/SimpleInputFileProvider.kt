package pw.forst.olb.hub.providers

import pw.forst.olb.common.dto.job.JobData
import java.io.File
import java.nio.file.Files

/**
 * Uses temp file storage
 * */
class SimpleInputFileProvider : InputFileProvider {

    override fun createFile(data: JobData): File {
        val dir = Files.createTempDirectory("olb-folder-job-${data.id}-")
        Files.createFile(dir.resolve(data.jobInput.fileName)).toFile().writeText(data.jobInput.json) //TODO serialize whole jobInput
        return dir.toFile()
    }
}
