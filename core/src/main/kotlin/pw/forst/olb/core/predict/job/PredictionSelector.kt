package pw.forst.olb.core.predict.job

import pw.forst.olb.common.dto.scheduling.JobPlanView

interface PredictionSelector {

    fun getPredictor(jobPlanView: JobPlanView): JobValuePrediction
}
