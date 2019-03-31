package pw.forst.olb.core.predict.fitting

import mu.KLogging
import pw.forst.olb.core.predict.fitting.leastsquares.HyperbolaFitting

class NonLinearHyperbolaRegression : AbstractPrediction<DoubleArray>() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running polynomial prediction!" } }

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: DoubleArray?): Y? {
        if (data.size < 2) return null

        val fitter = HyperbolaFitting(params ?: determineInitialParameters(data))
        val xs = mutableListOf<Double>()
        val ys = mutableListOf<Double>()

        data.forEach { (x, y) -> xs.add(x); ys.add(y) }

        return fitter.predict(xs = xs, ys = ys, x = toPredict, params = params ?: determineInitialParameters(data))
    }

    private fun determineInitialParameters(data: Map<X, Y>): DoubleArray {

        return oneFreeVariable(data)
    }

//    private fun noFreeVariable(data: Map<X, Y>): DoubleArray {
//        val xs = data.keys.shuffled().take(3)
//        val ys = listOf(data.getValue(xs[0]), data.getValue(xs[1]))
//        val c = (xs[0] * ys[0] - xs[1] * ys[1]) / (ys[0] - ys[1])
//        val b = xs[1] * ys[1] - c * ys[2]
//        return doubleArrayOf(0.0, b, c)
//    }

    private fun oneFreeVariable(data: Map<X, Y>): DoubleArray {
        val xs = data.keys.shuffled().take(2)
        val ys = listOf(data.getValue(xs[0]), data.getValue(xs[1]))
        val c = (xs[0] * ys[0] - xs[1] * ys[1]) / (ys[0] - ys[1])
        val b = xs[1] * ys[1] - c * ys[1]
        return doubleArrayOf(0.01, b, c)
    }
}

