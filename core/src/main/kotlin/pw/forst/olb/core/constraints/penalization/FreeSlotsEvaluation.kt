package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.dto.sum
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder
import pw.forst.olb.core.domain.Plan

/**
 * Aim is to fill each computer in full
 * */
class FreeSlotsEvaluation : PlanEvaluation {
    override fun calculatePenalty(plan: Plan): Penalty = PenaltyBuilder.create().apply {
        soft(
            -plan.assignments.filter { !it.isValid }.map { it.cost }.sum().value * 10
//            -plan.assignments.count { !it.isValid } * 10
        )
    }.get()

}
