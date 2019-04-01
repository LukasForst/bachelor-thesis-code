package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.domain.Plan

interface PlanEvaluation {
    fun calculatePenalty(plan: Plan): Penalty
}
