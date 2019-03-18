package pw.forst.olb.core.predict.job

import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.scheduling.JobPlanView

interface JobValuePrediction {

    fun predict(view: JobPlanView, iteration: Iteration): JobValue?
}
