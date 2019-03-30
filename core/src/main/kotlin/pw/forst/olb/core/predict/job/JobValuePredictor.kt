//package pw.forst.olb.core.predict.job
//
//import pw.forst.olb.common.dto.job.Iteration
//import pw.forst.olb.common.dto.job.JobValue
//import pw.forst.olb.common.dto.impl.JobValueImpl
//import pw.forst.olb.common.dto.scheduling.JobPlanView
//import pw.forst.olb.common.extensions.mapKeysAndValues
//import pw.forst.olb.core.predict.fitting.PredictionWithParameter
//
//class JobValuePredictor<T>(
//    private val prediction: PredictionWithParameter<T>,
//    private val predictionParameter: T?
//) : JobValuePrediction {
//
//    override fun predict(view: JobPlanView, iteration: Iteration): JobValue? {
//        val predictionSet = view.jobMeta.values.mapKeysAndValues({ (key, _) -> key.position.toDouble() }, { (_, job) -> job.hard })
//        return prediction.predict(predictionSet, iteration.position.toDouble(), predictionParameter)?.let { JobValueImpl(it) }
//    }
//}
