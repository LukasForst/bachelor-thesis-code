package pw.forst.olb.common.dto.planning

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import java.util.SortedMap

data class Plan(
    val assignments: SortedMap<Time, Map<Job, JobAssignment>>
)
