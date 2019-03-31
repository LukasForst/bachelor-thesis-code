package pw.forst.olb.core.predict.fitting.predictors

import mu.KLogging
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.util.Pair
import pw.forst.olb.core.predict.fitting.HyperbolaFunction
import pw.forst.olb.core.predict.fitting.HyperbolicDerivatives
import pw.forst.olb.core.predict.fitting.HyperbolicParameters
import pw.forst.olb.core.predict.fitting.HyperbolicRegression
import pw.forst.olb.core.predict.fitting.parametrization.X
import pw.forst.olb.core.predict.fitting.parametrization.Y
import pw.forst.olb.core.predict.fitting.toDoubleArray

class ApacheHyperbolicRegression(
    private val maxIterations: Int,
    private val maxEvaluations: Int,

    hyperbolaFunction: HyperbolaFunction,
    initialGuessLambda: (Map<X, Y>) -> HyperbolicParameters,
    dataPreprocessor: ((Map<X, Y>) -> Map<X, Y>)?
) : HyperbolicRegression(hyperbolaFunction, initialGuessLambda, dataPreprocessor) {

    private companion object : KLogging() {
        const val dimension: Int = 3
    }

    override fun obtainParametersUnsafe(data: Map<X, Y>, initialGuess: HyperbolicParameters?): HyperbolicParameters? {
        if (data.size < 4)
            return null.also { logger.error { "It is not possible to run hyperbolic prediction because there are no data! Data size: ${data.size}" } }

        val xs = data.keys.toList().sorted()
        val ys = xs.map { data.getValue(it) }

        val jacobianFunction = MultivariateJacobianFunction { point ->
            val parameters = point.toParameters()

            val modelValue = ArrayRealVector(xs.size)
            val jacobian = Array2DRowRealMatrix(xs.size, dimension)

            xs.zip(ys).forEachIndexed { index, (x, y) ->
                modelValue.setEntry(index, hyperbolaFunction.modelFunction(parameters, x, y))
                jacobian.setDerivatives(index, hyperbolaFunction.hyperbolicDerivatives(parameters, x, y))
            }
            Pair<RealVector, RealMatrix>(modelValue, jacobian)
        }

        val problem = LeastSquaresBuilder()
            .start((initialGuess ?: initialGuessLambda(data)).toDoubleArray())
            .model(jacobianFunction)
            .target(xs.zip(ys) { x, y -> x * y }.toDoubleArray())
            .lazyEvaluation(false)
            .maxEvaluations(maxEvaluations)
            .maxIterations(maxIterations)
            .build()

        return LevenbergMarquardtOptimizer()
            .optimize(problem)
            .point
            .toParameters()
    }

    private fun Array2DRowRealMatrix.setDerivatives(index: Int, derivatives: HyperbolicDerivatives) {
        val (da, db, dc) = derivatives
        this.setEntry(index, 0, da)
        this.setEntry(index, 1, db)
        this.setEntry(index, 2, dc)
    }

    private fun RealVector.toParameters(): HyperbolicParameters =
        HyperbolicParameters(a = this.getEntry(0), b = this.getEntry(1), c = this.getEntry(2))
}
