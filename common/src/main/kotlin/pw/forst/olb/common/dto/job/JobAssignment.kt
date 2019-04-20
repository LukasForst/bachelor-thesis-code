package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.ResourcesAllocation

interface JobAssignment : Comparable<JobAssignment> {

    val job: Job?

    val time: Time

    val allocation: ResourcesAllocation?

    val cost: Cost

    val isMovable: Boolean

    fun toCompleteAssignment(): CompleteJobAssignment?

    val isValid: Boolean
        get() = job != null && allocation != null

    override fun compareTo(other: JobAssignment): Int = time.compareTo(other.time)
}

interface CompleteJobAssignment : JobAssignment {
    override val job: Job

    override val time: Time

    override val allocation: ResourcesAllocation

    override val cost: Cost
        get() = allocation.cost

    override fun toCompleteAssignment(): CompleteJobAssignment = this
}
