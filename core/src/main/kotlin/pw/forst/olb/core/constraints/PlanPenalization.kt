package pw.forst.olb.core.constraints

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty

interface PlanPenalization {

    fun calculatePenalty(jobView: JobPlanView): Penalty
}
