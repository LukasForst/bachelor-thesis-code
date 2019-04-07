package pw.forst.olb.common.dto

import java.util.concurrent.TimeUnit

interface Time : Comparable<Time> {
    val position: Long

    val units: TimeUnit

    override fun compareTo(other: Time): Int = this.position.compareTo(other.position)

    operator fun plus(other: Time): Time

    operator fun minus(other: Time): Time

    operator fun times(times: Long): Time

    operator fun times(times: Int): Time
}

data class TimeImpl(
    override val position: Long,
    override val units: TimeUnit
) : Time {

    companion object {
        val default: Time = TimeImpl(-1, TimeUnit.SECONDS)
    }

    override operator fun plus(other: Time) = copy(position = this.position + other.position)
    override operator fun minus(other: Time) = copy(position = this.position - other.position)
    override operator fun times(times: Long) = copy(position = this.position * times)
    override operator fun times(times: Int) = copy(position = this.position * times)
}


class TimeIterator(private val start: Time, private val endInclusive: Time, private val step: Time) : Iterator<Time> {
    private var current: Time? = null

    override fun hasNext(): Boolean = getOrNull() != null

    override fun next(): Time = getOrNull()?.also { current = it } ?: throw NoSuchElementException()

    fun toList(): List<Time> {
        val result = mutableListOf<Time>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    private fun getOrNull(): Time? = if (current == null) start else (current!! + step).let { if (it > endInclusive) null else it }
}

data class TimeRangeBuilder(val from: Time, val to: Time)

infix fun Time.until(to: Time) = TimeRangeBuilder(from = this, to = to)

infix fun TimeRangeBuilder.withStep(step: Time): TimeIterator = TimeIterator(this.from, this.to, step)
