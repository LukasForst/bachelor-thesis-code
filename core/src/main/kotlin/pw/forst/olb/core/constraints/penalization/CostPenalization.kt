package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.sumOnlyValues
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

class CostPenalization : CompletePlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        val maxCost = jobView.job.parameters.maxCost
        val assignmentsCost = jobView.assignments.map { it.cost }.sumOnlyValues()
        return createPenaltyForCost(assignmentsCost, maxCost)
    }

    private fun createPenaltyForCost(actualCost: Cost, maxCost: Cost): Penalty {
        val diff = (maxCost - actualCost).value

        return PenaltyBuilder
            .create()
            .hardIf(diff) { diff < 0.0 }
            .softIf(-diff) { diff > 0 }
            .get()
    }
}
