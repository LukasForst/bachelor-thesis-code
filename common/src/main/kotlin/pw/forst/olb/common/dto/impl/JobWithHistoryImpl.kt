package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Iteration
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
    override val iterationAllocationQuocient: (Iteration, ResourcesAllocation) -> Iteration,
    override val jobValueDuringIterations: Map<Iteration, JobValue>,
    override val allocationHistory: Map<Time, ResourcesAllocation>,
    override val iterationsInTimes: Map<Time, Collection<Iteration>>,
    override val iterationLengthInTimes: Map<Time, Collection<LengthAwareIteration>>,
    override val parameters: JobParameters,
    override val client: Client,
    override val name: String,
    override val uuid: UUID
) : JobWithHistory
