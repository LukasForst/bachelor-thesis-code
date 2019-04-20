package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation

data class CompleteJobAssignmentImpl(
    override val job: Job,
    override val time: Time,
    override val allocation: ResourcesAllocation,
    private val _isMovable: Boolean? = null
) : CompleteJobAssignment {
    override val isMovable: Boolean
        get() = _isMovable ?: false
}

fun completeJobAssignment(job: Job, time: Time, allocation: ResourcesAllocation): CompleteJobAssignment = CompleteJobAssignmentImpl(job, time, allocation)
