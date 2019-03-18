package pw.forst.olb.core.predict.fitting

import mu.KLogging
import pw.forst.olb.core.predict.fitting.leastsquares.HyperbolaFitting

class NonLinearHyperbolaRegression : AbstractPrediction<DoubleArray>() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running polynomial prediction!" } }

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: DoubleArray?): Y? {
        val fitter = if (params != null) HyperbolaFitting(params) else HyperbolaFitting()
        val xs = mutableListOf<Double>()
        val ys = mutableListOf<Double>()

        data.forEach { (x, y) -> xs.add(x); ys.add(y) }

        return fitter.predict(xs, ys, toPredict)
    }
}

