package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation

data class SimpleCompleteJobAssignment(
    override val job: Job,
    override val time: Time,
    override val allocation: ResourcesAllocation
) : CompleteJobAssignment

fun completeJobAssignment(job: Job, time: Time, allocation: ResourcesAllocation): CompleteJobAssignment = SimpleCompleteJobAssignment(job, time, allocation)
