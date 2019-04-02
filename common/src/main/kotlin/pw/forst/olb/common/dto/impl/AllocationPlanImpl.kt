package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool
import java.util.UUID

data class AllocationPlanImpl(
    override val uuid: UUID,
    override val startTime: Time,
    override val endTime: Time,
    override val timeIncrement: Time,
    override val timeSchedule: Map<Time, Collection<JobResourcesAllocation>>,
    override val jobs: Collection<Job>,
    override val resourcesPools: Collection<ResourcesPool>,
    override val cost: Cost
) : AllocationPlan
