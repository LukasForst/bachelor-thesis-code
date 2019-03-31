package pw.forst.olb.core.predict.fitting

import pw.forst.olb.common.extensions.middleElement
import pw.forst.olb.core.predict.reduceDistribution

object DefaultHyperbolaConfiguration {

    const val defaultEvaluations = 1000

    const val defaultIterations = 1000

    const val defaultThreads = 2

    val initialGuessLambda: (Map<X, Y>) -> HyperbolicParameters = ::oneFreeVariableInitialGuess

    val hyperbolaFunction: () -> HyperbolaFunction = { HyperbolaFunction() }

    val dataPreprocessor: (Map<X, Y>) -> Map<X, Y> = { it.reduceDistribution() }

    private fun oneFreeVariableInitialGuess(data: Map<X, Y>): HyperbolicParameters {
        val xs = data.keys.filter { it != 0.0 }.shuffled().take(2)
        assert(xs.size < 2) { "It is not possible to guess data from less then 2 records!" }
        val ys = xs.map { data.getValue(it) }

        val a = data.getValue(data.keys.middleElement()!!)
        val c = (xs[0] * ys[0] - xs[1] * ys[1] - (xs[0] - xs[1]) * a) / (ys[1] - ys[0])
        val b = ys[0] * (xs[0] + c) - a * (xs[0] + c)

        return HyperbolicParameters(a = a, b = b, c = c)
    }
}
