package pw.forst.olb.core.predict.fitting.leastsquares

import mu.KLogging


class HyperbolaFitting(
    private val parameters: DoubleArray? = null
) {

    private companion object : KLogging() {
        val defaultParameters = doubleArrayOf(0.0, 1.0, 0.0)
    }


    fun predict(xs: List<Double>, ys: List<Double>, x: Double, params: DoubleArray = parameters ?: defaultParameters): Double? =
        runCatching { predict(xs.prepareTwoLevel(), ys.prepareOneLevel(), x, params) }
            .onFailure { logger.error(it) { "Exception during prediction!" } }
            .getOrNull()

    fun fit(xs: List<Double>, ys: List<Double>, params: DoubleArray = parameters ?: defaultParameters): List<Double>? =
        runCatching { fit(xs.prepareTwoLevel(), ys.prepareOneLevel(), params).toList() }
            .onFailure { logger.error(it) { "Exception during fitting!" } }
            .getOrNull()

    private fun predict(xs: Array<Array<Double>>, ys: Array<Double>, x: Double, params: DoubleArray) =
        fitParameters(xs, ys, params).let { HyperbolaFunction().evaluate(doubleArrayOf(x), it) }

    private fun fit(xs: Array<Array<Double>>, ys: Array<Double>, params: DoubleArray): List<Double> =
        fitParameters(xs, ys, params).toList()

    private fun fitParameters(xs: Array<Array<Double>>, ys: Array<Double>, params: DoubleArray) =
        with(NonLinearSolver(HyperbolaFunction())) {
            setData(xs.map { it.toDoubleArray() }.toTypedArray(), ys.toDoubleArray())
            parameters = params
            fitData()
            parameters
        }

    private fun Collection<Double>.prepareTwoLevel() = this.map { arrayOf(it) }.toTypedArray()

    private fun Collection<Double>.prepareOneLevel() = this.toTypedArray()

    private class HyperbolaFunction : Function {
        override val nParameters: Int = 3

        override val nInputs: Int = 1

        override fun evaluate(values: DoubleArray, parameters: DoubleArray): Double {
            val a = parameters[0]
            val b = parameters[1]
            val c = parameters[2]
            val x = values[0]
            return a + b / (x + c)
        }
    }
}
