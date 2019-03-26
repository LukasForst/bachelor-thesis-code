package pw.forst.olb.core.domain

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.variable.PlanningVariable
import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.completeJobAssignment
import pw.forst.olb.common.dto.invalidCost
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.resources.ResourcesAllocation

@PlanningEntity
data class PlanJobAssignment(

    @field:PlanningVariable(valueRangeProviderRefs = ["jobRange"])
    override val job: Job? = null,

    @field:PlanningVariable(valueRangeProviderRefs = ["resourcesRange"])
    override val allocation: ResourcesAllocation? = null,

    @field:PlanningVariable(valueRangeProviderRefs = ["timeRange"])
    override val time: Time? = null


) : JobAssignment {

    override val isValid: Boolean
        get() = job != null && time != null && allocation != null

    override val cost: Cost
        get() = if (isValid) allocation!!.cost else invalidCost()

    override fun toCompleteAssignment(): CompleteJobAssignment? = if (isValid) completeJobAssignment(job!!, time!!, allocation!!) else null
}
