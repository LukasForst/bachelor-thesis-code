package pw.forst.olb.core.evaluator

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView

class JobAssignmentValueProviderImpl : JobAssignmentValueProvider {
    override fun jobAssignmentValue(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): JobValue? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
