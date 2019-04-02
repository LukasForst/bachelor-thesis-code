package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool

interface AllocationPlan : GenericPlan {

    val timeSchedule: Map<Time, Collection<JobResourcesAllocation>>

    val jobs: Collection<Job>

    val resourcesPools: Collection<ResourcesPool>

    val cost: Cost
}
