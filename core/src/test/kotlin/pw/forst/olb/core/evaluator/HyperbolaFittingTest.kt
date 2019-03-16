package pw.forst.olb.core.evaluator

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView
import kotlin.test.assertNotNull
import kotlin.test.fail

internal class HyperbolaFittingTest {

    private fun generateObjects(data: Map<Int, Double>): Map<Time, JobValue> =
        data
            .mapKeys { (x, _) -> Time(x.toLong()) }
            .mapValues { (_, y) -> JobValueImpl(y) }

    private fun obtainView(lowestX: Int, highestX: Int, function: (Int) -> Double): JobPlanView = mock<JobPlanView> {
        on { values } doReturn generateObjects(
            generateSequence(lowestX) { (it + 1).takeIf { result -> result < highestX } }.associate { it to function(it) }
        )
    }

    private fun assertResult(expected: Double, actual: Double, allowedDifference: Double = 0.0001) {
        if (Math.abs(expected - actual) > allowedDifference) {
            fail("Difference is too big! - expected: $expected, actual: $actual")
        }
    }


    @Test
    fun `simple hyperbole`() {
        val function: (Int) -> Double = { 1.0 / it }

        val view = obtainView(1, 100, function)

        val result = HyperbolaFitting().jobAssignmentValue(view, mock(), Time(100))

        assertNotNull(result)
        assertResult(function(100), result.value)
    }

    @Test
    fun `y shifted hyperbole`() {
        val function: (Int) -> Double = { 1 + 1.0 / it }

        val view = obtainView(1, 100, function)

        val result = HyperbolaFitting().jobAssignmentValue(view, mock(), Time(100))

        assertNotNull(result)
        assertResult(function(100), result.value)
    }

    @Test
    fun `x shifted hyperbole`() {
        val function: (Int) -> Double = { 1.0 / (it + 1) }

        val view = obtainView(1, 100, function)

        val result = HyperbolaFitting().jobAssignmentValue(view, mock(), Time(100))

        assertNotNull(result)
        assertResult(function(100), result.value)
    }

    @Test
    fun `upper shifted hyperbole`() {
        val function: (Int) -> Double = { 1 + (100 * 1.0) / (it) }

        val view = obtainView(1, 100, function)

        val result = HyperbolaFitting(doubleArrayOf(10.0, 50.0, 0.0)).jobAssignmentValue(view, mock(), Time(100))

        assertNotNull(result)
        assertResult(function(100), result.value)
    }


}
