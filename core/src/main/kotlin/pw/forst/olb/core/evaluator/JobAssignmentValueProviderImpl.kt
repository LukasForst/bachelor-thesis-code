package pw.forst.olb.core.evaluator

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView
import pw.forst.olb.core.predict.job.PredictionSelector

class JobAssignmentValueProviderImpl(
    private val predictionSelector: PredictionSelector
) : JobAssignmentValueProvider {

    override fun jobAssignmentValue(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): JobValue? {
        //here transform time into iteration and send it to the actual fitting function

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
