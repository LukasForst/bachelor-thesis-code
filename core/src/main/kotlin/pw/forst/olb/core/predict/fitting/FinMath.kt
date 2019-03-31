package pw.forst.olb.core.predict.fitting

import net.finmath.optimizer.LevenbergMarquardt


class FinMathOptimization : AbstractPrediction<Int>() {
    private fun List<Double>.normalize(): List<Double> {
        val sum = this.sum()
        return this.map { it / sum }
    }

    override fun getParameters(data: Map<X, Y>): List<Double> {
        val keys = data.keys.toList().sorted()
        val values = keys.map { data.getValue(it) }

        return FinMath(4, keys, values)
            .setInitialParameters(oneFreeVariable(data))
            .setWeights(keys.normalize().toDoubleArray())
            .setMaxIteration(1000)
            .setTargetValues(keys.zip(values) { x, y -> x * y }.toDoubleArray())
            .also { it.run() }
            .bestFitParameters
            .toList()
    }

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: Int?): Y? {
        if (data.size < 10) return null
        val (a, b, c) = Parameters(getParameters(data).toDoubleArray())
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
    private val xs: List<Double>,
    private val ys: List<Double>
) : LevenbergMarquardt(threads) {

    override fun setDerivatives(parameters: DoubleArray, derivatives: Array<out DoubleArray>) {
        val (a, _, c) = Parameters(parameters)
        xs.zip(ys).forEachIndexed { i, (x, y) ->
            derivatives[0][i] = c + x
            derivatives[1][i] = 1.0
            derivatives[2][i] = a - y
        }
    }

    override fun setValues(parameters: DoubleArray, values: DoubleArray) {
        val (a, b, c) = Parameters(parameters)
        xs.zip(ys).forEachIndexed { i, (x, y) ->
            values[i] = a * c + a * x + b - y * c
        }
    }

}

