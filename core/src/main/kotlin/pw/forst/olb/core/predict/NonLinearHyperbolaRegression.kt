package pw.forst.olb.core.predict

import mu.KLogging
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView
import pw.forst.olb.core.predict.leastsquares.HyperbolaFitting

class NonLinearHyperbolaRegression(
    private val initialParameters: DoubleArray? = null
) : AbstractPrediction() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while fitting hyperbola!" } }

    override fun predictUnSafe(view: JobPlanView, time: Time): JobValue? {
        val fitter = if (initialParameters != null) HyperbolaFitting(initialParameters) else HyperbolaFitting()
        val xs = mutableListOf<Double>()
        val ys = mutableListOf<Double>()

        view.values.forEach { (time, value) -> xs.add(time.seconds.toDouble()); ys.add(value.value) }

        return fitter.predict(xs, ys, time.seconds.toDouble())?.let { JobValueImpl(it) }
    }
}
