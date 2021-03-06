package pw.forst.olb.common.dto

import pw.forst.olb.common.extensions.inSeconds
import java.io.Serializable
import java.util.concurrent.TimeUnit

interface Time : Comparable<Time>, Serializable {
    val position: Long

    val units: TimeUnit

    override fun compareTo(other: Time): Int = units.toMillis(position).compareTo(other.units.toMillis(other.position))

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

    override operator fun plus(other: Time) = copy(position = this.units.toSeconds(position) + other.units.toSeconds(other.position), units = TimeUnit.SECONDS)
    override operator fun minus(other: Time) = copy(position = this.units.toSeconds(position) - other.units.toSeconds(other.position), units = TimeUnit.SECONDS)
    override operator fun times(times: Long) = copy(position = this.position * times)
    override operator fun times(times: Int) = copy(position = this.position * times)

    override fun toString(): String {
        return "Time(position=$position)"
    }
}


data class TimeIterator(private val start: Time, private val endInclusive: Time, private val step: Time) : Iterator<Time>, Iterable<Time> {
    override fun iterator(): Iterator<Time> = copy()

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

infix fun TimeRangeBuilder.withStep(step: Time): TimeIterator = TimeIterator(this.from.inSeconds(), this.to.inSeconds(), step.inSeconds())
