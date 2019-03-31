package pw.forst.olb.core.predict.fitting

import mu.KLogging
import net.finmath.optimizer.LevenbergMarquardt
import pw.forst.olb.common.extensions.normalize


class FinMathRegression(
    private val threads: Int,
    private val maxIterations: Int,

    hyperbolaFunction: HyperbolaFunction,
    initialGuessLambda: (Map<X, Y>) -> HyperbolicParameters,
    dataPreprocessor: ((Map<X, Y>) -> Map<X, Y>)?
) : HyperbolicRegression(hyperbolaFunction, initialGuessLambda, dataPreprocessor) {

    private companion object : KLogging()

    override fun obtainParametersUnsafe(data: Map<X, Y>, initialGuess: HyperbolicParameters?): HyperbolicParameters? {
        if (data.size < 4)
            return null.also { logger.error { "It is not possible to run hyperbolic prediction because there are no data! Data size: ${data.size}" } }

        val xs = data.keys.toList().sorted()
        val ys = xs.map { data.getValue(it) }

        return HyperbolaOptimizerBuilder.create()
            .setXs(xs)
            .setYs(ys)
            .setFunction(hyperbolaFunction)
            .useThreads(threads)
            .createOptimizationBuilder()
            .setInitialParameters(
                (initialGuess ?: initialGuessLambda(data)).toDoubleArray()
            )
            // since xs are sorted by asc, weight should be more at the end of the list
            .setWeights(
                xs.map { it * it }.normalize().toDoubleArray()
            )
            .setMaxIteration(maxIterations)
            .setTargetValues(
                xs.zip(ys) { x, y -> x * y }.toDoubleArray()
            )
            .also {
                it.run()
                logger.info { "Finished in ${it.iterations} iterations!" }
            }
            .bestFitParameters.toHyperbolicParameters()
    }


    private class HyperbolaOptimizerBuilder private constructor() {
        private lateinit var xs: List<Double>
        private lateinit var ys: List<Double>

        private var func: HyperbolaFunction = DefaultHyperbolaConfiguration.hyperbolaFunction()
        private var threads: Int = DefaultHyperbolaConfiguration.defaultThreads

        companion object {
            fun create(): HyperbolaOptimizerBuilder = HyperbolaOptimizerBuilder()
        }

        fun setXs(xs: List<Double>): HyperbolaOptimizerBuilder {
            this.xs = xs
            return this
        }

        fun setYs(ys: List<Double>): HyperbolaOptimizerBuilder {
            this.ys = ys
            return this
        }

        fun setFunction(func: HyperbolaFunction): HyperbolaOptimizerBuilder {
            this.func = func
            return this
        }

        fun useThreads(threads: Int): HyperbolaOptimizerBuilder {
            this.threads = threads
            return this
        }

        fun createOptimizationBuilder(): LevenbergMarquardt {
            return HyperbolicLevenbergMarquardt(
                xs = xs,
                ys = ys,
                func = func,
                threads = threads
            )
        }

    }

    private class HyperbolicLevenbergMarquardt(
        private val xs: List<Double>,
        private val ys: List<Double>,
        private val func: HyperbolaFunction,
        threads: Int
    ) : LevenbergMarquardt(threads) {

        override fun setDerivatives(parameters: DoubleArray, derivatives: Array<out DoubleArray>) {
            xs.zip(ys).forEachIndexed { i, (x, y) ->
                val (da, db, dc) = func.hyperbolicDerivatives(HyperbolicParameters(parameters), x, y)
                derivatives[0][i] = da
                derivatives[1][i] = db
                derivatives[2][i] = dc
            }
        }

        override fun setValues(parameters: DoubleArray, values: DoubleArray) {
            xs.zip(ys).forEachIndexed { i, (x, y) ->
                values[i] = func.modelFunction(HyperbolicParameters(parameters), x, y)
            }
        }
    }
}
