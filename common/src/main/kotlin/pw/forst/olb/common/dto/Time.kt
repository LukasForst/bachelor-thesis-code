package pw.forst.olb.common.dto

data class Time(
    val seconds: Long
) : Comparable<Time> {

    companion object {

        val zero = Time(0)
    }

    init {
        //TODO enable this check
//        if (seconds < 0) throw IllegalArgumentException("It is not possible have negative time!")
    }

    override fun compareTo(other: Time): Int = this.seconds.compareTo(other.seconds)

    operator fun plus(other: Time) = Time(this.seconds + other.seconds)

    operator fun minus(other: Time) = Time(this.seconds - other.seconds)

    operator fun times(times: Long) = Time(this.seconds * times)
    operator fun times(times: Int) = Time(this.seconds * times)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Time

        if (seconds != other.seconds) return false

        return true
    }

    override fun hashCode(): Int {
        return seconds.hashCode()
    }
}


class TimeIterator(private val start: Time, private val endInclusive: Time, private val step: Time) : Iterator<Time> {
    private var current: Time? = null

    override fun hasNext(): Boolean = getOrNull() != null

    override fun next(): Time = getOrNull()?.also { current = it } ?: throw NoSuchElementException()

    private fun getOrNull(): Time? = if (current == null) start else (current!! + step).let { if (it > endInclusive) null else it }
}

data class TimeRangeBuilder(val from: Time, val to: Time)

infix fun Time.until(to: Time) = TimeRangeBuilder(from = this, to = to)

infix fun TimeRangeBuilder.step(step: Time): TimeIterator = TimeIterator(this.from, this.to, step)
