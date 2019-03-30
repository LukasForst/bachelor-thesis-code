//package pw.forst.olb.core.predict.job
//
//import pw.forst.olb.common.dto.Time
//import pw.forst.olb.common.dto.job.JobValue
//import pw.forst.olb.common.dto.resources.ResourcesAllocation
//import pw.forst.olb.common.dto.scheduling.JobPlanView
//import pw.forst.olb.core.predict.IterationPredict
//
//class JobAssignmentValueProviderImpl(
//    private val predictionSelector: PredictionSelector,
//    private val iterationConverter: IterationPredict
//) : JobAssignmentValueProvider {
//
//    override fun jobAssignmentValue(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): JobValue? {
//        val finalIteration = iterationConverter.getIteration(jobView.jobMeta, time, allocation)
//
//        return predictionSelector.getPredictor(jobView).predict(jobView, finalIteration)
//    }
//
//}
