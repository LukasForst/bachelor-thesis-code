package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import java.util.UUID

data class SimpleJob(

    override val parameters: JobParameters,

    override val client: Client,

    override val uuid: UUID,

    override val name: String = uuid.toString()
) : Job {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleJob

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}
