package pw.forst.olb.core.predict.fitting

import mu.KLogging
import pw.forst.olb.core.predict.fitting.parametrization.X
import pw.forst.olb.core.predict.fitting.parametrization.Y

abstract class HyperbolicRegression(
    protected val hyperbolaFunction: HyperbolaFunction,
    protected val initialGuessLambda: (Map<X, Y>) -> HyperbolicParameters,
    private val dataPreprocessor: ((Map<X, Y>) -> Map<X, Y>)?
) : AbstractPrediction<HyperbolicParameters>() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running hyperbolic regression!" } }

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: HyperbolicParameters?): Y? {
        val parameters = obtainParameters(data, params) ?: return null
        return hyperbolaFunction.getY(parameters, toPredict)
    }

    fun obtainParameters(data: Map<X, Y>, initialGuess: HyperbolicParameters? = null): HyperbolicParameters? =
        runCatching { obtainParametersUnsafe(dataPreprocessor?.invoke(data) ?: data, initialGuess) }
            .onFailure(logFailure)
            .getOrNull()

    protected abstract fun obtainParametersUnsafe(data: Map<X, Y>, initialGuess: HyperbolicParameters? = null): HyperbolicParameters?
}




