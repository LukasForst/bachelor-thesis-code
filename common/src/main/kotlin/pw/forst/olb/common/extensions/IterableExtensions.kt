package pw.forst.olb.common.extensions

import java.util.ArrayList
import java.util.Random


/**
 * Function that will return a random element from the iterable
 */
fun <E> Iterable<E>.getRandomElement(rand: Random) = this.elementAt(rand.nextInt(this.count()))

/**
 * Creates reduction of the given [Iterable]. This function can be used for example for cumulative sums
 */
fun <T, R> Iterable<T>.reduction(initial: R, operation: (acc: R, T) -> R): List<R> {
    val result = ArrayList<R>()
    var last = initial
    for (item in this) {
        last = operation(last, item)
        result.add(last)
    }
    return result
}

/**
 * Returns the sumCosts of all values produced by [selector] function applied to each element in the collection.
 */
inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * Returns the sumCosts of all values produced by [selector] function applied to each element in the sequence.
 *
 * The operation is _terminal_.
 */
inline fun <T> Sequence<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * Sums all Lists of integers into single one by indexes (i.e. all the numbers with the same index are always summed together). If the lists have different
 * lengths, the final list has length corresponding to the shortest list in [this] iterable.
 */
fun Iterable<List<Int>>.sumByIndexes(): List<Int> {
    val minSize = this.minValueBy { it.size } ?: throw IllegalArgumentException("Only nonempty collections are supported.")
    val result = MutableList(minSize) { 0 }

    for (index in 0 until minSize) {
        for (list in this) {
            result[index] += list[index]
        }
    }
    return result
}

/**
 * Returns the largest value of the given function or `null` if there are no elements.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.maxValueBy(selector: (T) -> R): R? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var maxValue = selector(iterator.next())
    while (iterator.hasNext()) {
        val v = selector(iterator.next())
        if (maxValue < v) {
            maxValue = v
        }
    }
    return maxValue
}

/**
 * Returns the smallest value of the given function or `null` if there are no elements.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.minValueBy(selector: (T) -> R): R? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var minValue = selector(iterator.next())
    while (iterator.hasNext()) {
        val v = selector(iterator.next())
        if (minValue > v) {
            minValue = v
        }
    }
    return minValue
}

/**
 * Applies the given [transform] function to each element of the original collection
 * and returns results as Set
 */
inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(LinkedHashSet(), transform)
}

/**
 * Applies the given [transform] function to each element of the original collection
 * and returns results as Set
 */
inline fun <T, R> Iterable<T>.flatMapToSet(transform: (T) -> Iterable<R>): Set<R> {
    return flatMapTo(LinkedHashSet(), transform)
}

/**
 * Returns the most frequently occurring value of the given function or `null` if there are no elements.
 */
fun <T, R> Iterable<T>.dominantValueBy(selector: (T) -> R): R? = this.groupingBy(selector).eachCount().maxBy { it.value }?.key

/**
 * Creates cartesian product between all the elements from [this] and [other] iterable. E.g. when [this] contains [1,2,3] and [other] contains ['a', 'b'], the
 * result will be {Pair(1,'a'), Pair(1,'b'), Pair(2,'a'), Pair(2,'b'), Pair(3,'a'), Pair(3,'b')}.
 */
fun <T1, T2> Iterable<T1>.cartesianProduct(other: Iterable<T2>): Set<Pair<T1, T2>> {
    val result = LinkedHashSet<Pair<T1, T2>>(this.count() * other.count())
    for (item1 in this) {
        for (item2 in other) {
            result.add(Pair(item1, item2))
        }
    }
    return result
}

/**
 * Performs the given [action] on each element that is not null.
 */
inline fun <T : Any> Iterable<T?>.forEachNotNull(action: (T) -> Unit) {
    for (element in this) element?.let(action)
}

/**
 * Creates union of the given iterables
 */
fun <T> Iterable<Iterable<T>?>.union(): Set<T> {
    val result = LinkedHashSet<T>()
    this.forEachNotNull { input -> result.addAll(input) }
    return result
}

/**
 * Creates intersection of the given iterables
 */
fun <T> Iterable<Iterable<T>?>.intersect(): Set<T> {
    val result = LinkedHashSet<T>()
    var first = true
    for (item in this) {
        if (item == null) continue
        if (first) {
            first = false
            result.addAll(item)
        } else result.retainAll(item)
    }
    return result
}

/**
 * Returns a list containing only the non-null results of applying the given transform function to each element in the original collection.
 */
fun <T : Any, R : Any> Iterable<T?>.filterNotNullBy(selector: (T) -> R?): List<T> {
    val result = ArrayList<T>()
    this.mapNotNull { }
    for (item in this) {
        if (item != null && selector(item) != null) result.add(item)
    }
    return result
}

/**
 * Returns the single element matching the given [predicate], or `null` if element was not found.
 *
 * Throws [IllegalArgumentException] when multiple elements are matching predicate
 */
inline fun <T> Iterable<T>.singleOrEmpty(predicate: (T) -> Boolean): T? {
    var single: T? = null
    var found = false
    for (element in this) {
        if (predicate(element)) {
            if (found) throw IllegalArgumentException("Collection contains more than one matching element.")
            single = element
            found = true
        }
    }
    return single
}


/**
 * Returns single element, or `null` if the collection is empty
 * Throws [IllegalArgumentException] when multiple elements are matching predicate
 */
fun <T> Iterable<T>.singleOrEmpty(): T? {
    when (this) {
        is List -> return if (size == 0) null else if (size == 1) this[0] else throw IllegalArgumentException("Collection contains more than one element.")
        else -> {
            val iterator = iterator()
            if (!iterator.hasNext())
                return null
            val single = iterator.next()
            if (iterator.hasNext())
                throw IllegalArgumentException("Collection contains more than one element.")
            return single
        }
    }
}

/**
 * Takes Iterable with pairs and returns pair of collections filled with values in each part of pair
 * */
fun <T, V> Iterable<Pair<T, V>>.splitPairCollection(): Pair<List<T>, List<V>> {
    val ts = mutableListOf<T>()
    val vs = mutableListOf<V>()
    for ((t, v) in this) {
        ts.add(t)
        vs.add(v)
    }
    return ts to vs
}

/**
 * Returns all values that are in [this] and not in [other] with custom [selector]
 * */
inline fun <T, R> Iterable<T>.setDifferenceBy(other: Iterable<T>, selector: (T) -> R): List<T> =
    (this.distinctBy(selector).map { Pair(it, true) } + other.distinctBy(selector).map { Pair(it, false) })
        .groupBy { selector(it.first) }
        .filterValues { it.size == 1 && it.single().second }
        .map { (_, value) -> value.single().first }

internal const val INT_MAX_POWER_OF_TWO: Int = Int.MAX_VALUE / 2 + 1

/**
 * Calculate the initial capacity of a map, based on Guava's com.google.common.collect.Maps approach. This is equivalent
 * to the Collection constructor for HashSet, (c.size()/.75f) + 1, but provides further optimisations for very small or
 * very large sizes, allows support non-collection classes, and provides consistency for all map based class construction.
 */
@PublishedApi
internal fun mapCapacity(expectedSize: Int): Int {
    if (expectedSize < 3) {
        return expectedSize + 1
    }
    if (expectedSize < INT_MAX_POWER_OF_TWO) {
        return expectedSize + expectedSize / 3
    }
    return Int.MAX_VALUE // any large value
}

/**
 * Returns the size of this iterable if it is known, or the specified [default] value otherwise.
 */
@PublishedApi
internal fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int = if (this is Collection<*>) this.size else default

/**
 * Returns average of collection
 * */
inline fun <T> Collection<T>.averageByDouble(selector: (T) -> Double): Double = this.map(selector).average()


/**
 * Returns average of collection
 * */
inline fun <T> Collection<T>.averageByInt(selector: (T) -> Int): Double = this.map(selector).average()

/**
 * Returns average of collection
 * */
inline fun <T> Collection<T>.averageByLong(selector: (T) -> Long): Double = this.map(selector).average()

/**
 * Returns average of collection
 * */
inline fun <T> Collection<T>.averageByFloat(selector: (T) -> Float): Double = this.map(selector).average()

/**
 * Returns min and max or null
 * */
inline fun <T, R : Comparable<R>> Collection<T>.minMaxBy(selector: (T) -> R): Pair<T, T>? = minMaxBy(selector, selector)


/**
 * Returns min and max or null
 * */
inline fun <T, R : Comparable<R>> Collection<T>.minMaxBy(minSelector: (T) -> R, maxSelector: (T) -> R): Pair<T, T>? {
    if (this.isEmpty()) return null

    val min = this.minBy(minSelector) ?: return null
    val max = this.maxBy(maxSelector) ?: return null
    return min to max
}

inline fun <T, R : Comparable<R>> Collection<T>.minMaxValueBy(selector: (T) -> R): Pair<R, R>? = minMaxValueBy(selector, selector)

fun <T : Comparable<T>> Collection<T>.minMaxValue(): Pair<T, T>? = minMaxValueBy { it }

/**
 * Returns min and max or null
 * */
inline fun <T, R : Comparable<R>> Collection<T>.minMaxValueBy(minSelector: (T) -> R, maxSelector: (T) -> R): Pair<R, R>? {
    if (this.isEmpty()) return null

    val min = this.minValueBy(minSelector) ?: return null
    val max = this.maxValueBy(maxSelector) ?: return null
    return min to max
}

fun <T> List<T>.middleElement(): T? = if (size == 0) null else get(size / 2)

fun <T> Collection<T>.middleElement(): T? =
    when {
        this is List -> this.middleElement()
        this.isEmpty() -> null
        else -> this.elementAt(size / 2)
    }

fun Collection<Double>.normalize(): Collection<Double> {
    val sum = this.sum()
    return this.map { it / sum }
}

inline fun <T, R> Iterable<T>.foldWithNext(initial: R, operation: (acc: R, a: T, b: T) -> R): R {
    var accumulator = initial

    val iterator = iterator()
    if (!iterator.hasNext()) return accumulator

    var current = iterator.next()
    while (iterator.hasNext()) {
        val next = iterator.next()
        accumulator = operation(accumulator, current, next)
        current = next
    }

    return accumulator
}
