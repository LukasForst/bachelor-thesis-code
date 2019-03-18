package pw.forst.olb.core.constraints.preconditions

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.scheduling.JobPlanView

class CostJobSchedulingPrecondition : JobSchedulingPrecondition {

    override fun useInCurrentRound(jobPlanView: JobPlanView, time: Time): Boolean = jobPlanView.totalCost < jobPlanView.job.parameters.maxCost
}
