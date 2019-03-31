package pw.forst.olb.core.predict.fitting

class NoExtrapolation : HyperbolicRegression(
    HyperbolaFunction(),
    DefaultHyperbolaConfiguration.initialGuessLambda,
    null
) {
    override fun obtainParametersUnsafe(data: Map<X, Y>, initialGuess: HyperbolicParameters?): HyperbolicParameters? {
        return initialGuess ?: HyperbolicParameters(0.0, 1.0, 0.0)
    }
}
