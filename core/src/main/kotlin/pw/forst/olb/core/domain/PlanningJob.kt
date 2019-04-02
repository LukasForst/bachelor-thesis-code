package pw.forst.olb.core.domain

import org.optaplanner.core.api.domain.lookup.PlanningId
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import java.util.UUID

data class PlanningJob(
    override val parameters: JobParameters,
    override val client: Client,
    override val name: String,

    @PlanningId
    override val uuid: UUID
) : Job {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanningJob

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}
