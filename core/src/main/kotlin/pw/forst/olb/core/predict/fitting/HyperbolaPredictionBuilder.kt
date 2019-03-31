package pw.forst.olb.core.predict.fitting

class HyperbolaPredictionBuilder private constructor() {
    private var maxThreads: Int = DefaultHyperbolaConfiguration.defaultThreads
    private var maxIterations: Int = DefaultHyperbolaConfiguration.defaultIterations
    private var maxEvaluations: Int = DefaultHyperbolaConfiguration.defaultEvaluations

    private var initialGuessLambda: (Map<X, Y>) -> HyperbolicParameters = DefaultHyperbolaConfiguration.initialGuessLambda

    private var func: HyperbolaFunction = DefaultHyperbolaConfiguration.hyperbolaFunction()

    private var dataPreprocessor: ((Map<X, Y>) -> Map<X, Y>)? = DefaultHyperbolaConfiguration.dataPreprocessor

    companion object {
        fun create() = HyperbolaPredictionBuilder()
    }

    fun setMaxIterations(maxIterations: Int): HyperbolaPredictionBuilder {
        this.maxIterations = maxIterations
        return this
    }

    fun setMaxEvaluations(maxEvaluations: Int): HyperbolaPredictionBuilder {
        this.maxEvaluations = maxEvaluations
        return this
    }

    fun setMaxUsedThreads(maxThreads: Int): HyperbolaPredictionBuilder {
        this.maxThreads = maxThreads
        return this
    }

    fun setInitialGuessFunction(initialGuessLambda: (Map<X, Y>) -> HyperbolicParameters): HyperbolaPredictionBuilder {
        this.initialGuessLambda = initialGuessLambda
        return this
    }

    fun setFunction(hyperbolaFunction: HyperbolaFunction): HyperbolaPredictionBuilder {
        this.func = hyperbolaFunction
        return this
    }

    fun setDataPreprocessor(dataPreprocessor: (Map<X, Y>) -> Map<X, Y>): HyperbolaPredictionBuilder {
        this.dataPreprocessor = dataPreprocessor
        return this
    }

    fun buildWithApache(): ApacheHyperbolicRegression {
        return ApacheHyperbolicRegression(
            maxIterations = maxIterations,
            maxEvaluations = maxEvaluations,
            hyperbolaFunction = func,
            initialGuessLambda = initialGuessLambda,
            dataPreprocessor = dataPreprocessor
        )
    }

    fun buildWithFinMath(): FinMathRegression {
        return FinMathRegression(
            threads = maxThreads,
            maxIterations = maxIterations,
            hyperbolaFunction = func,
            initialGuessLambda = initialGuessLambda,
            dataPreprocessor = dataPreprocessor
        )
    }

    fun build(): HyperbolicRegression {
        return buildWithFinMath()
    }
}
