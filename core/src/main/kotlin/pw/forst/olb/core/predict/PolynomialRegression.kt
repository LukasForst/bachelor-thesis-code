package pw.forst.olb.core.predict

import mu.KLogging
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView


class PolynomialRegression : AbstractPrediction() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running polynomial prediction!" } }

    override fun predictUnSafe(view: JobPlanView, time: Time): JobValue {
        val polynomialCurveFitter = PolynomialCurveFitter.create(2)

        val points = WeightedObservedPoints()
        view.values.forEach { (time, value) -> points.add(time.seconds.toDouble(), value.value) }
        val values = polynomialCurveFitter.fit(points.toList())

        return JobValueImpl(value = values[0] + values[1] * time.seconds + values[2] * time.seconds * time.seconds)
    }
}
