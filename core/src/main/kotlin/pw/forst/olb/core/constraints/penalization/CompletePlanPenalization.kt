package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty

interface CompletePlanPenalization {

    fun calculatePenalty(jobView: JobPlanView): Penalty
}
