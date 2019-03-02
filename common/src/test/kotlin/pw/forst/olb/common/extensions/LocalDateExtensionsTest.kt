package pw.forst.olb.common.extensions

import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.streams.toList
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LocalDateExtensionsTest {

    @Test
    fun testGetDateRangeTo() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        assertEquals(daysTight, from.getDateRangeTo(toTight))

        val toLoose = LocalDate.of(2018, 8, 11)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 11))
        assertEquals(daysLoose, from.getDateRangeTo(toLoose))

        val toInfeasible = LocalDate.of(2018, 8, 9)
        val daysInfeasible = listOf<LocalDate>()
        assertEquals(daysInfeasible, from.getDateRangeTo(toInfeasible))
    }

    @Test
    fun testGetDateRangeToAsStream() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        assertEquals(daysTight, from.getDateRangeToAsStream(toTight).toList())

        val toLoose = LocalDate.of(2018, 8, 11)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 11))
        assertEquals(daysLoose, from.getDateRangeToAsStream(toLoose).toList())

        val toInfeasible = LocalDate.of(2018, 8, 9)
        val daysInfeasible = listOf<LocalDate>()
        assertEquals(daysInfeasible, from.getDateRangeToAsStream(toInfeasible).toList())
    }

    @Test
    fun testGetInvertedDateRangeToAsStream() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        val daysTight = listOf(LocalDate.of(2018, 8, 10))
        assertEquals(daysTight, from.getInvertedDateRangeToAsStream(toTight).toList())

        val toLoose = LocalDate.of(2018, 8, 9)
        val daysLoose = listOf(LocalDate.of(2018, 8, 10), LocalDate.of(2018, 8, 9))
        assertEquals(daysLoose, from.getInvertedDateRangeToAsStream(toLoose).toList())

        val toInfeasible = LocalDate.of(2018, 8, 11)
        val daysInfeasible = listOf<LocalDate>()
        assertEquals(daysInfeasible, from.getInvertedDateRangeToAsStream(toInfeasible).toList())
    }

    @Test
    fun testGetDaysInInterval() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        assertEquals(1, from.getDaysInInterval(toTight))

        val toLoose = LocalDate.of(2018, 8, 11)
        assertEquals(2, from.getDaysInInterval(toLoose))

        val toInfeasible = LocalDate.of(2018, 8, 9)
        assertFailsWith<IllegalArgumentException> { from.getDaysInInterval(toInfeasible) }
    }

    @Test
    fun testGetDayDifference() {
        val from = LocalDate.of(2018, 8, 10)
        val toTight = LocalDate.of(2018, 8, 10)
        assertEquals(0, from.getDayDifference(toTight))

        val toLoose = LocalDate.of(2018, 8, 11)
        assertEquals(1, from.getDayDifference(toLoose))

        val toNegative = LocalDate.of(2018, 8, 9)
        assertEquals(-1, from.getDayDifference(toNegative))
    }

}
