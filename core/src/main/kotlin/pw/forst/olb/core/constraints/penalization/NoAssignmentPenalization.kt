package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

class NoAssignmentPenalization : CompletePlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty = PenaltyBuilder.create()
        .apply {
            hardIf(-1) { jobView.assignments.isEmpty() }
            soft(jobView.assignments.size)
        }.get()

}
