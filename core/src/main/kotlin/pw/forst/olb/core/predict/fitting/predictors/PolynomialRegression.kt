package pw.forst.olb.core.predict.fitting.predictors

import mu.KLogging
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import pw.forst.olb.core.predict.fitting.AbstractPrediction
import pw.forst.olb.core.predict.fitting.parametrization.X
import pw.forst.olb.core.predict.fitting.parametrization.Y
import java.lang.Math.pow


class PolynomialRegression : AbstractPrediction<Int>() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running polynomial prediction!" } }

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: Int?): Y? {
        val degree = params ?: 2

        if (data.size < degree) return null.also { logger.warn { "No data to predict!" } }

        val polynomialCurveFitter = PolynomialCurveFitter.create(degree)

        val points = WeightedObservedPoints()

        data.forEach { (x, y) -> points.add(x, y) }
        val values = polynomialCurveFitter.fit(points.toList())

        return (0..degree).fold(0.0) { v, i -> v + values[i] * pow(toPredict, i.toDouble()) }
    }
}
