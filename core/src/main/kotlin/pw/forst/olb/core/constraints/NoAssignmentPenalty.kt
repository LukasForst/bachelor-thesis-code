package pw.forst.olb.core.constraints

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

class NoAssignmentPenalty : PlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty =
        with(PenaltyBuilder.create()) {
            hardIf(-1) { jobView.assignments.isEmpty() }
            soft(jobView.assignments.size)
            get()
        }

}
