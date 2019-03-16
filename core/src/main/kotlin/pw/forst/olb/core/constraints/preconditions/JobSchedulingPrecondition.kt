package pw.forst.olb.core.constraints.preconditions

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.scheduling.JobPlanView

interface JobSchedulingPrecondition {

    fun useInCurrentRound(jobPlanView: JobPlanView, time: Time): Boolean
}
