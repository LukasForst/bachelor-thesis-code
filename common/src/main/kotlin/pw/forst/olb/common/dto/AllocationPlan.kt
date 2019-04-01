package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool

interface AllocationPlan {

    val start: Time

    val end: Time

    val granularity: Time

    val timeSchedule: Map<Time, JobResourcesAllocation>

    val jobs: Collection<Job>

    val resourcesPools: Collection<ResourcesPool>

    val cost: Cost
}
