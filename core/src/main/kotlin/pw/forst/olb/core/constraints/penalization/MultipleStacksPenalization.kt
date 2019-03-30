package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.extensions.mapToSet
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

class MultipleStacksPenalization : CompletePlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty = PenaltyBuilder.create()
        .apply {
            jobView.assignments
                .groupBy { it.time }
                .map { (_, assignments) -> assignments.mapToSet { it.allocation.provider }.size }
                .forEach { hardIf(-it) { it > 1 } }
        }.get()
}
