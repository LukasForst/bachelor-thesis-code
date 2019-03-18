package pw.forst.olb.core.constraints

import pw.forst.olb.common.dto.scheduling.PlanView

interface PlanConstraint {

    fun feasiblePlan(planView: PlanView): Boolean
}
