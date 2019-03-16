package pw.forst.olb.core.evaluator

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import mu.KLogging
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView
import pw.forst.olb.core.predict.LinearRegression
import pw.forst.olb.core.predict.NonLinearHyperbolaRegression
import pw.forst.olb.core.predict.PolynomialRegression
import pw.forst.olb.core.predict.Prediction
import java.util.stream.Stream
import kotlin.test.assertNotNull
import kotlin.test.fail

internal class HyperbolaFittingTest {

    private companion object : KLogging() {

        @Suppress("unused") //used as parameter
        @JvmStatic
        fun generatePredictions(): Stream<Arguments> = Stream.of(
            Arguments.of(NonLinearHyperbolaRegression(), 0.0001),
            Arguments.of(LinearRegression(), Double.MAX_VALUE), //not necessary tested on hyperbolas
            Arguments.of(PolynomialRegression(), Double.MAX_VALUE) //not necessary tested on hyperbolas
        )
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `simple hyperbole`(prediction: Prediction, allowedDifference: Double) {
        val function: (Int) -> Double = { 1.0 / it }

        val view = obtainView(1, 100, function)

        val predictedX = 300
        val result = prediction.predict(view, Time(predictedX.toLong()))

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `y shifted hyperbole`(prediction: Prediction, allowedDifference: Double) {
        val function: (Int) -> Double = { 1 + 1.0 / it }

        val view = obtainView(1, 100, function)

        val predictedX = 100
        val result = prediction.predict(view, Time(predictedX.toLong()))

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `x shifted hyperbole`(prediction: Prediction, allowedDifference: Double) {
        val function: (Int) -> Double = { 1.0 / (it + 1) }

        val view = obtainView(1, 100, function)

        val predictedX = 100
        val result = prediction.predict(view, Time(predictedX.toLong()))

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `upper shifted hyperbole`(prediction: Prediction, allowedDifference: Double) {
        val function: (Int) -> Double = { 1 + (50 * 1.0) / (it) }

        val view = obtainView(1, 100, function)

        val predictedX = 300
        val result = prediction.predict(view, Time(predictedX.toLong()))

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }


    private fun generateObjects(data: Map<Int, Double>): Map<Time, JobValue> =
        data
            .mapKeys { (x, _) -> Time(x.toLong()) }
            .mapValues { (_, y) -> JobValueImpl(y) }

    private fun obtainView(lowestX: Int, highestX: Int, function: (Int) -> Double): JobPlanView = mock {
        on { values } doReturn generateObjects(
            generateSequence(lowestX) { (it + 1).takeIf { result -> result < highestX } }.associate { it to function(it) }
        )
    }

    private fun assertResult(expected: Double, actual: Double, allowedDifference: Double = 0.0001) {
        val diff = Math.abs(expected - actual)
        if (diff > allowedDifference) {
            fail("Difference is too big! Diff $diff - expected: $expected, actual: $actual")
        }
        logger.info { "Diff: $diff OK - Expected: $expected, Actual: $actual" }
    }
}
