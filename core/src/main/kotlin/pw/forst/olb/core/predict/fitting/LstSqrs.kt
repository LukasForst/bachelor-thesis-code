package pw.forst.olb.core.predict.fitting

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import pw.forst.olb.core.predict.reduceDistribution
import org.apache.commons.math3.util.Pair as ApachePair


class LstSqrs : HyperbolicRegression(
    DefaultHyperbolaConfiguration.hyperbolaFunction(),
    DefaultHyperbolaConfiguration.initialGuessLambda,
    null
) {
    override fun obtainParametersUnsafe(data: Map<X, Y>, initialGuess: HyperbolicParameters?): HyperbolicParameters? = getParameters(data).toHyperbolicParameters()

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: HyperbolicParameters?): Y? {
        if (data.size < 10) return null

        val prms = getParameters(data)
        return prms[0] + prms[1] / (toPredict + prms[2])
    }

    private data class Parameters(val a: Double, val b: Double, val c: Double)

    private data class Observation(val x: Double, val y: Double)

    private fun modelFunction(parameters: Parameters, x: Double, y: Double): Double {
        val (a, b, c) = parameters
        return a * c + a * x + b - y * c
    }

    private fun jacobianFunction(parameters: Parameters, x: Double, y: Double): DoubleArray {
        val (a, _, c) = parameters
        return doubleArrayOf(x + c, 1.0, a - y)
    }


    fun getParameters(data: Map<X, Y>): List<Double> {
        val reducedData = data.reduceDistribution()
        val observedPoints = reducedData.map { (x, y) -> Observation(x = x, y = y) }

        val criterialFunction = MultivariateJacobianFunction { point ->

            val modelValue = ArrayRealVector(observedPoints.size)
            val jacobian = Array2DRowRealMatrix(observedPoints.size, 3)

            val parameters = Parameters(point.getEntry(0), point.getEntry(1), point.getEntry(2))
            observedPoints.forEachIndexed { index, (x, y) ->
                modelValue.setEntry(index, modelFunction(parameters, x, y))
                jacobianFunction(parameters, x, y)
                    .forEachIndexed { idx, d -> jacobian.setEntry(index, idx, d) }
            }

            ApachePair<RealVector, RealMatrix>(modelValue, jacobian)
        }

        // least squares problem to solve : modeled radius should be close to target radius
        val problem = LeastSquaresBuilder()
            .start(oneFreeVariable(reducedData))
//            .start(doubleArrayOf(0.0, 1.0, 0.0))
            .model(criterialFunction)
            .target(observedPoints.map { it.x * it.y }.toDoubleArray())
            .lazyEvaluation(false)
            .maxEvaluations(1000)
            .maxIterations(1000)
            .build()

        val optimum = LevenbergMarquardtOptimizer()
            .optimize(problem)

        val finalParameters = Parameters(a = optimum.point.getEntry(0), b = optimum.point.getEntry(1), c = optimum.point.getEntry(2))

        return listOf(finalParameters.a, finalParameters.b, finalParameters.c)
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
