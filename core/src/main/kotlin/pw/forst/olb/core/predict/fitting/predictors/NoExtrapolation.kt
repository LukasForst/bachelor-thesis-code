package pw.forst.olb.core.predict.fitting.predictors

import pw.forst.olb.core.predict.fitting.HyperbolaFunction
import pw.forst.olb.core.predict.fitting.HyperbolicParameters
import pw.forst.olb.core.predict.fitting.HyperbolicRegression
import pw.forst.olb.core.predict.fitting.parametrization.DefaultHyperbolaConfiguration
import pw.forst.olb.core.predict.fitting.parametrization.X
import pw.forst.olb.core.predict.fitting.parametrization.Y

class NoExtrapolation : HyperbolicRegression(
    HyperbolaFunction(),
    DefaultHyperbolaConfiguration.initialGuessLambda,
    null
) {
    override fun obtainParametersUnsafe(data: Map<X, Y>, initialGuess: HyperbolicParameters?): HyperbolicParameters? {
        return initialGuess ?: HyperbolicParameters(0.0, 1.0, 0.0)
    }
}
