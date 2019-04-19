package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.job.LengthAwareIteration
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import java.util.UUID

data class JobWithHistoryImpl(
    override val plan: GenericPlan,
    override val schedulingStartedTime: Time,
    override val averageIteration: LengthAwareIteration,
    override val jobValueDuringIterations: Map<Iteration, JobValue>,
    override val iterationLengthInTimes: Map<Time, Collection<LengthAwareIteration>>,
    override val parameters: JobParameters,
    override val client: Client,
    override val name: String,
    override val uuid: UUID,
    override val allocationHistory: Map<Time, ResourcesAllocation>? = null,
    private val _iterationAllocationQuocient: (Iteration, ResourcesAllocation, JobWithHistory) -> Iteration,
    private val _iterationsInTimes: Map<Time, Collection<Iteration>>? = null
) : JobWithHistory {

    constructor(
        plan: GenericPlan,
        schedulingStartedTime: Time,
        averageIteration: LengthAwareIteration,
        jobValueDuringIterations: Map<Iteration, JobValue>,
        iterationLengthInTimes: Map<Time, Collection<LengthAwareIteration>>,
        job: Job,
        allocationHistory: Map<Time, ResourcesAllocation>? = null,
        _iterationAllocationQuotient: (Iteration, ResourcesAllocation, JobWithHistory) -> Iteration,
        _iterationsInTimes: Map<Time, Collection<Iteration>>? = null
    ) : this(
        plan,
        schedulingStartedTime,
        averageIteration,
        jobValueDuringIterations,
        iterationLengthInTimes,
        job.parameters,
        job.client,
        job.name,
        job.uuid,
        allocationHistory,
        _iterationAllocationQuotient,
        _iterationsInTimes
    )

    override fun iterationAllocationQuocient(iteration: Iteration, allocation: ResourcesAllocation): Iteration = _iterationAllocationQuocient.invoke(iteration, allocation, this)

    override fun toString(): String {
        return "JobWithHistoryImpl(name='$name')"
    }

    override val iterationsInTimes: Map<Time, Collection<Iteration>>
        get() = _iterationsInTimes ?: iterationLengthInTimes
}
