package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.extensions.minMaxBy
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder
import pw.forst.olb.core.constraints.penalty.PenaltyFactory

class TimePenalization : CompletePlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        val maxTime = jobView.job.parameters.maxTime
        val (firstAssignment, lastAssignment) = jobView.assignments.minMaxBy { it.time } ?: return PenaltyFactory.noPenalty
        return createPenaltyForTimeDifference(lastAssignment.time - firstAssignment.time, maxTime)
    }

    private fun createPenaltyForTimeDifference(actualTime: Time, jobMaxTime: Time): Penalty {
        val diff = (jobMaxTime - actualTime).position.toDouble()

        return PenaltyBuilder
            .create()
            .hardIf(diff) { diff < 0.0 }
            .softIf(-diff) { diff > 0 }
            .get()
    }

}
