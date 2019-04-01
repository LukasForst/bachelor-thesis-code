package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.JobWithHistory

interface AllocationPlanWithHistory : AllocationPlan {
    val jobsData: Collection<JobWithHistory>
}
