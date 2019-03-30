package pw.forst.olb.core.domain

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty
import org.optaplanner.core.api.domain.variable.PlanningVariable
import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.completeJobAssignment
import pw.forst.olb.common.dto.invalidCost
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.core.constraints.filter.ResourcesSelectionFilter

@PlanningEntity(
    movableEntitySelectionFilter = ResourcesSelectionFilter::class
)
data class PlanJobAssignment(

    @field:PlanningVariable(valueRangeProviderRefs = ["jobRange"], nullable = true)
    override val job: Job? = null,

    @field:ProblemFactProperty
    override val allocation: ResourcesAllocation? = null,

    @field:ProblemFactProperty
    override val time: Time? = null
) : JobAssignment {

    override val isValid: Boolean
        get() = job != null && time != null && allocation != null

    override val cost: Cost
        get() = allocation?.cost ?: invalidCost()

    override fun toCompleteAssignment(): CompleteJobAssignment? = if (isValid) completeJobAssignment(job!!, time!!, allocation!!) else null
}
