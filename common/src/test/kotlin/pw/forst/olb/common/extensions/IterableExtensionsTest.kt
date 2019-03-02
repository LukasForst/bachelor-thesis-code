package pw.forst.olb.common.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IterableExtensionsTest {

    @Test
    fun testSumByLong() {
        val longs = listOf(10L, 20L, 30L)
        assertEquals(120L, longs.sumByLong { it * 2 })
        val empty = listOf<Long>()
        assertEquals(0, empty.sumByLong { it * 2 })
    }

    @Test
    fun testSumByLongSequence() {
        val longs = sequenceOf(10L, 20L, 30L)
        assertEquals(120L, longs.sumByLong { it * 2 })
        val empty = sequenceOf<Long>()
        assertEquals(0, empty.sumByLong { it * 2 })
    }

    @Test
    fun testReduction() {
        val values = listOf(1, 5, 7, 3, 1, 8)
        val cumSum = values.reduction(0) { a, b -> a + b }
        assertEquals(listOf(1, 6, 13, 16, 17, 25), cumSum)
        val cumProd = values.reduction(1) { a, b -> a * b }
        assertEquals(listOf(1, 5, 35, 105, 105, 840), cumProd)
    }

    @Test
    fun testSumByIndexes() {
        val empty: Iterable<List<Int>> = listOf()
        assertFailsWith<Exception> { empty.sumByIndexes() }

        val single = listOf(List(4) { it })
        assertEquals(single.first(), single.sumByIndexes())

        val multiple = listOf(List(5) { 2 * it + 1 }, List(4) { it }, List(4) { 1 })
        val result = listOf(2, 5, 8, 11)
        assertEquals(result, multiple.sumByIndexes())
    }

    @Test
    fun testMaxValueBy() {
        val empty = listOf<Int>()
        assertNull(empty.maxValueBy { it })

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        assertEquals(5, data.maxValueBy { it.first })
        assertEquals("l", data.maxValueBy { it.second })
    }

    @Test
    fun testMinValueBy() {
        val empty = listOf<Int>()
        assertNull(empty.minValueBy { it })

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        assertEquals(0, data.minValueBy { it.first })
        assertEquals("a", data.minValueBy { it.second })
    }

    @Test
    fun testDominantValueBy() {
        val empty = listOf<Int>()
        assertNull(empty.dominantValueBy { it })

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        assertEquals(5, data.dominantValueBy { it.first })
        assertEquals("a", data.minValueBy { it.second })
    }

    @Test
    fun testCartesianProduct() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf('a', 'b')
        val output = setOf(Pair(1, 'a'), Pair(1, 'b'), Pair(2, 'a'), Pair(2, 'b'), Pair(3, 'a'), Pair(3, 'b'))
        assertEquals(output, input1.cartesianProduct(input2))
    }

    @Test
    fun testForEachNotNull() {
        val result = ArrayList<Int>()
        val input = listOf(1, 2, null, 3, 4, null)
        val expected = listOf(1, 2, 3, 4)
        input.forEachNotNull { result.add(it) }
        assertEquals(expected, result)
    }

    @Test
    fun testUnion() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf(3, 4, 5)
        val input3 = listOf(5, 6, 7)
        val output = setOf(1, 2, 3, 4, 5, 6, 7)
        assertEquals(output, listOf(input1, input2, input3).union())
    }

    @Test
    fun testIntersect() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf(3, 4, 5)
        val input3 = listOf(5, 3, 7)
        val output = setOf(3)
        assertEquals(output, listOf(input1, input2, input3).intersect())
    }

    @Test
    fun testFilterNotNullBy() {
        val input = listOf(1 to 2, null, 2 to null, 3 to 4, 5 to 6)
        val output = listOf(1 to 2, 3 to 4, 5 to 6)
        assertEquals(output, input.filterNotNullBy { it.second })
    }

    @Test
    fun `test singleOrEmpty`() {
        val input = listOf(1, 2, 3, 4, 5)
        assertEquals(5, input.singleOrEmpty { it > 4 })
        assertNull(input.singleOrEmpty { it > 5 })
        assertFailsWith(IllegalArgumentException::class) { input.singleOrEmpty { it > 3 } }

        assertEquals(1, listOf(1).singleOrEmpty())
        assertNull(emptyList<Int>().singleOrEmpty())
        assertFailsWith(IllegalArgumentException::class) { input.singleOrEmpty() }
    }

    @Test
    fun `test splitPairCollection`() {
        val input = listOf(1 to 2, 3 to 4, 5 to 6)
        val (odd, even) = input.splitPairCollection()
        assertEquals(listOf(1, 3, 5), odd)
        assertEquals(listOf(2, 4, 6), even)
    }

    @Test
    fun `test setDifferenceBy`() {
        val `this` = listOf(1 to 1, 2 to 1, 3 to 1)
        val other = listOf(1 to 2, 2 to 2, 5 to 2)
        val result = `this`.setDifferenceBy(other) { it.first }
        assertTrue { result.size == 1 }
        assertTrue { result.single() == 3 to 1 }
    }
}
