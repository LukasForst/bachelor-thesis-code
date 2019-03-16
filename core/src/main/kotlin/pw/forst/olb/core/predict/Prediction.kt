package pw.forst.olb.core.predict

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.scheduling.JobPlanView

interface Prediction {

    fun predict(view: JobPlanView, time: Time): JobValue?
}
