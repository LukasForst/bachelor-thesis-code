package pw.forst.olb.core.predict

import pw.forst.olb.common.extensions.merge
import pw.forst.olb.common.extensions.middleElement

fun <K : Comparable<K>, V : Any> Map<K, V>.reduceDistribution(reducedChunk: Int, selectionStrategy: KeySelectionStrategy): Map<K, V> =
    when (selectionStrategy) {
        KeySelectionStrategy.MIDDLE -> this.reduceDistribution(reducedChunk) { it.middleElement() }
        KeySelectionStrategy.MIN -> this.reduceDistribution(reducedChunk) { it.min() }
        KeySelectionStrategy.MAX -> this.reduceDistribution(reducedChunk) { it.max() }
    }

inline fun <K : Comparable<K>, V : Any> Map<K, V>.reduceDistribution(reducedChunk: Int, newKeySelectionStrategy: (List<K>) -> K?): Map<K, V> {
    return this.entries
        .groupBy({ (_, value) -> value }, { (key, _) -> key })
        .mapValues { (_, keys) ->
            keys.sorted()
                .chunked(reducedChunk)
                .mapNotNull { newKeySelectionStrategy(it) }
        }
        .map { (value, keys) -> keys.associate { it to value } }
        .merge()
        .mapValues { (_, v) -> v.single() }
}

fun <K : Comparable<K>, V : Any> Map<K, V>.reduceDistribution(): Map<K, V> = reduceDistribution(KeySelectionStrategy.MIDDLE)

fun <K : Comparable<K>, V : Any> Map<K, V>.reduceDistribution(selectionStrategy: KeySelectionStrategy): Map<K, V> =
    when (selectionStrategy) {
        KeySelectionStrategy.MIDDLE -> this.reduceDistribution { it.middleElement() }
        KeySelectionStrategy.MIN -> this.reduceDistribution { it.min() }
        KeySelectionStrategy.MAX -> this.reduceDistribution { it.max() }
    }

inline fun <K : Comparable<K>, V : Any> Map<K, V>.reduceDistribution(newKeySelectionStrategy: (List<K>) -> K?): Map<K, V> =
    this.entries
        .groupBy({ (_, value) -> value }, { (key, _) -> key })
        .mapValues { (_, keys) -> newKeySelectionStrategy(keys) }
        .mapNotNull { (value, key) -> key?.to(value) }
        .toMap()

enum class KeySelectionStrategy {
    MIDDLE,
    MIN,
    MAX
}

