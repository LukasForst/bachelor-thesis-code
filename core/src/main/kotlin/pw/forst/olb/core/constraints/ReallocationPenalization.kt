package pw.forst.olb.core.constraints

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyFactory

class ReallocationPenalization : PlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        //TODO

        return PenaltyFactory.noPenalty
    }
}
