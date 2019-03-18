package pw.forst.olb.core.evaluator

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import mu.KLogging
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView
import pw.forst.olb.common.extensions.mapKeysAndValues
import pw.forst.olb.core.predict.job.JobValuePrediction
import pw.forst.olb.core.predict.job.JobValuePredictorFactory
import kotlin.test.assertNotNull
import kotlin.test.fail

internal class HyperbolaFittingTest {

    private companion object : KLogging() {

        @Suppress("unused") //used as parameter
        @JvmStatic
        fun generatePredictions(): Collection<Arguments> = listOf(
            Arguments.of(JobValuePredictorFactory.nonLinearHyperbolaRegression(), 0.0001),
            Arguments.of(JobValuePredictorFactory.linearRegression(), Double.MAX_VALUE), //not necessary tested on hyperbolas
            Arguments.of(JobValuePredictorFactory.polynomialRegression(), Double.MAX_VALUE) //not necessary tested on hyperbolas
        )
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `simple hyperbole`(prediction: JobValuePrediction, allowedDifference: Double) {
        val function: (Long) -> Double = { 1.0 / it }

        val view = obtainView(1, 100, function)

        val predictedX = 300L
        val result = prediction.predict(view, mock { on { it.position } doReturn predictedX })

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `y shifted hyperbole`(prediction: JobValuePrediction, allowedDifference: Double) {
        val function: (Long) -> Double = { 1 + 1.0 / it }

        val view = obtainView(1, 100, function)

        val predictedX = 100L
        val result = prediction.predict(view, mock { on { it.position } doReturn predictedX })

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `x shifted hyperbole`(prediction: JobValuePrediction, allowedDifference: Double) {
        val function: (Long) -> Double = { 1.0 / (it + 1) }

        val view = obtainView(1, 100, function)

        val predictedX = 100L
        val result = prediction.predict(view, mock { on { it.position } doReturn predictedX })

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }

    @ParameterizedTest
    @MethodSource("generatePredictions")
    fun `upper shifted hyperbole`(prediction: JobValuePrediction, allowedDifference: Double) {
        val function: (Long) -> Double = { 1 + (50 * 1.0) / (it) }

        val view = obtainView(1, 100, function)

        val predictedX = 300L
        val result = prediction.predict(view, mock { on { it.position } doReturn predictedX })

        assertNotNull(result)
        assertResult(function(predictedX), result.value, allowedDifference)
    }


    private fun generateObjects(data: Map<Long, Double>): Map<Iteration, JobValue> =
        data.mapKeysAndValues({ key -> SimpleIteration(position = key.key) }, { JobValueImpl(it.value) })

    private fun obtainView(lowestX: Long, highestX: Long, function: (Long) -> Double): JobPlanView = mock {
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

    private data class SimpleIteration(
        override val position: Long = 0L,
        override val iterationLengthInMls: Int = 0

    ) : Iteration
}
