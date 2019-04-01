package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyFactory

class ReallocationEvaluation : CompletePlanEvaluation {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        //TODO

        return PenaltyFactory.noPenalty
    }
}
