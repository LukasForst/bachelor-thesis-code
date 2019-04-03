package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.extensions.maxValueBy
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder
import pw.forst.olb.core.constraints.penalty.PenaltyFactory

class TimeEvaluation : CompletePlanEvaluation {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        val maxTime = jobView.job.parameters.maxTime + jobView.plan.startTime
        val lastAssignment = jobView.assignments.maxValueBy { it.time } ?: return PenaltyFactory.noPenalty
        return createPenaltyForTimeDifference(lastAssignment - jobView.plan.startTime, maxTime)
    }

    private fun createPenaltyForTimeDifference(actualTime: Time, jobMaxTime: Time): Penalty {
        val diff = (jobMaxTime - actualTime).position.toDouble()

        return PenaltyBuilder
            .create()
            .hardIf(diff) { diff < 0.0 }
            .softIf(-diff * 10) { diff > 0 } // is this soft penalization necessary?
            .get()
    }

}
