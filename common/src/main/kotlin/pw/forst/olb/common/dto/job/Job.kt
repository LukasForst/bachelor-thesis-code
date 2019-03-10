package pw.forst.olb.common.dto.job

import java.util.UUID

data class Job(
    val parameters: JobParameters,

    val data: JobPlanningData,

    val jobPriority: Int = 0,

    val name: String? = null,

    val uuid: UUID = UUID.randomUUID()
) : Comparable<Job> {

    override fun compareTo(other: Job): Int = this.jobPriority.compareTo(other.jobPriority)

}
