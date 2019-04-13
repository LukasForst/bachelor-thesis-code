package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.ResourcesAllocation

interface JobWithHistory : Job {

    val plan: GenericPlan

    val schedulingStartedTime: Time

    val averageIteration: LengthAwareIteration

    val jobValueDuringIterations: Map<Iteration, JobValue>

    val allocationHistory: Map<Time, ResourcesAllocation>?

    val iterationsInTimes: Map<Time, Collection<Iteration>>

    val iterationLengthInTimes: Map<Time, Collection<LengthAwareIteration>>

    fun iterationAllocationQuocient(iteration: Iteration, allocation: ResourcesAllocation): Iteration
}
