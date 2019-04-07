package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.resources.ResourcesPool
import java.util.UUID

data class AllocationPlanWithHistoryImpl(
    override val jobsData: Collection<JobWithHistory>,
    override val timeSchedule: Map<Time, Collection<JobResourcesAllocation>>,
    override val resourcesPools: Collection<ResourcesPool>,
    override val cost: Cost,
    override val uuid: UUID,
    override val startTime: Time,
    override val endTime: Time,
    override val timeIncrement: Time
) : AllocationPlanWithHistory {

    override val jobs: Collection<Job>
        get() = jobsData
}
