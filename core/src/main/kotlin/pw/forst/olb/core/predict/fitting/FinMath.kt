package pw.forst.olb.core.predict.fitting

import net.finmath.optimizer.LevenbergMarquardt


class FinMathOptimization : AbstractPrediction<Int>() {
    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: Int?): Y? {
        if (data.size < 10) return null

        val keys = data.keys.filter { it != 0.0 }.toList()
        val values = keys.map { data.getValue(it) }

        val (a, b, c) = FinMath(params ?: 4, keys)
            .setInitialParameters(oneFreeVariable(data))
//            .setInitialParameters(doubleArrayOf(0.0,1.0,0.0))
            .setWeights(keys.map { 1.0 }.toDoubleArray())
            .setMaxIteration(1000)
            .setTargetValues(values.toDoubleArray())
            .also { it.run() }
            .bestFitParameters
            .let { Parameters(it) }

        return (a + (b / (toPredict + c)))
    }

    private fun oneFreeVariable(data: Map<X, Y>): DoubleArray {
        val xs = data.keys.filter { it != 0.0 }.shuffled().take(2)
        val ys = listOf(data.getValue(xs[0]), data.getValue(xs[1]))

        val a = data.getValue(data.keys.elementAt(data.size / 2))
        val c = (xs[0] * ys[0] - xs[1] * ys[1] - (xs[0] - xs[1]) * a) / (ys[1] - ys[0])
        val b = ys[0] * (xs[0] + c) - a * (xs[0] + c)
        return doubleArrayOf(a, b, c)
    }


}

data class Parameters(val a: Double, val b: Double, val c: Double) {
    constructor(parameters: DoubleArray) : this(parameters[0], parameters[1], parameters[2])
}

class FinMath(
    threads: Int,
    private val xs: List<Double>
) : LevenbergMarquardt(threads) {

    override fun setDerivatives(parameters: DoubleArray, derivatives: Array<out DoubleArray>) {
        val (a, _, c) = Parameters(parameters)
        xs.forEachIndexed { i, x ->
            derivatives[0][i] = c + x
            derivatives[1][i] = 1.0
            derivatives[2][i] = a
        }
    }

    override fun setValues(parameters: DoubleArray, values: DoubleArray) {
        val (a, b, c) = Parameters(parameters)
        xs.forEachIndexed { i, x ->
            values[i] = a + (b / (x + c))
        }
    }

}

