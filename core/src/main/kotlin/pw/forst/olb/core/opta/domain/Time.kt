package pw.forst.olb.core.opta.domain

data class Time(
    var position: Long = 0L
) : Comparable<Time> {

    companion object {
        const val periodDuration: Long = 2L

        val zero: Time = Time(position = 0)
    }

    override fun compareTo(other: Time): Int = this.position.compareTo(other.position)
}
