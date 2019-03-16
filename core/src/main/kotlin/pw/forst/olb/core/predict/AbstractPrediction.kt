package pw.forst.olb.core.predict

import mu.KLogging
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.scheduling.JobPlanView

abstract class AbstractPrediction : Prediction {
    private companion object : KLogging()

    override fun predict(view: JobPlanView, time: Time): JobValue? =
        runCatching { predictUnSafe(view, time) }
            .onFailure(logFailure)
            .getOrNull()

    protected open val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running polynomial prediction!" } }

    protected abstract fun predictUnSafe(view: JobPlanView, time: Time): JobValue?
}
