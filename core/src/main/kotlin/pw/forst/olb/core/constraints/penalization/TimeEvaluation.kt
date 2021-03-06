package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.extensions.maxValueBy
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder
import pw.forst.olb.core.constraints.penalty.PenaltyFactory

class TimeEvaluation : CompletePlanEvaluation {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        val lastAssignment = jobView.assignments.maxValueBy { it.time } ?: return PenaltyFactory.noPenalty
        return createPenaltyForTimeDifference(lastAssignment.toSeconds() - jobView.plan.startTime.toSeconds(), jobView.job.parameters.maxTime.toSeconds())
    }

    private fun Time.toSeconds() = this.units.toSeconds(this.position)

    private fun createPenaltyForTimeDifference(actualTime: Long, jobMaxTime: Long): Penalty {
        val diff = (jobMaxTime - actualTime).toDouble()

        return PenaltyBuilder
            .create()
            .hardIf(diff) { diff < 0.0 }
            .softIf(-diff * 10) { diff > 0 }
            .get()
    }

}
