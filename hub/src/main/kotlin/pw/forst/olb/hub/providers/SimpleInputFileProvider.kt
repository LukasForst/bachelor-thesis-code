package pw.forst.olb.hub.providers

import pw.forst.olb.common.dto.job.JobData
import java.io.File
import java.nio.file.Files

class SimpleInputFileProvider : InputFileProvider {

    override fun createFile(data: JobData): File {
        val dir = Files.createTempDirectory("olb-folder-")
        Files.createFile(dir.resolve(data.jobInput.fileName)).toFile().writeText(data.jobInput.json)
        return dir.toFile()
    }
}
