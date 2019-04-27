package pw.forst.olb.core.domain

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.lookup.PlanningId
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty
import org.optaplanner.core.api.domain.variable.PlanningVariable
import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.impl.completeJobAssignment
import pw.forst.olb.common.dto.invalidCost
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.core.constraints.moves.ResourcesSelectionFilter
import java.util.UUID

@PlanningEntity(
    movableEntitySelectionFilter = ResourcesSelectionFilter::class
)
data class PlanJobAssignment(
    @field:PlanningId
    val uuid: UUID,

    @field:PlanningVariable(valueRangeProviderRefs = ["jobRange"], nullable = true)
    override var job: Job? = null,

    @field:ProblemFactProperty
    override val allocation: ResourcesAllocation?,

    @field:ProblemFactProperty
    override val time: Time,

    @field:ProblemFactProperty
    override val isMovable: Boolean
) : JobAssignment {

    @Suppress("unused") // there has to be empty constructor for optaplanner
    private constructor() : this(UUID.randomUUID(), null, null, TimeImpl.default, true)

    override val isValid: Boolean
        get() = job != null && allocation != null

    override val cost: Cost
        get() = allocation?.cost ?: invalidCost()

    override fun toCompleteAssignment(): CompleteJobAssignment? = if (isValid) completeJobAssignment(job!!, time, allocation!!) else null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanJobAssignment

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}
