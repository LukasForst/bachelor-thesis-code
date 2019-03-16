package pw.forst.olb.core.predict

import mu.KLogging
import org.apache.commons.math3.stat.regression.SimpleRegression
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView

class LinearRegression : AbstractPrediction() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running linear prediction!" } }

    override fun predictUnSafe(view: JobPlanView, time: Time): JobValue? {
        if (view.totalSchedulingTime == Time.zero) return null.also { logger.info { "No data to predict!" } }

        val regression = SimpleRegression()
        view.values.forEach { (time, value) -> regression.addData(time.seconds.toDouble(), value.value) }
        val predicted = regression.predict(time.seconds.toDouble())
        if (predicted == Double.NaN) return null.also { logger.info { "It was not possible to create prediction!" } }

        return JobValueImpl(predicted)
    }
}
