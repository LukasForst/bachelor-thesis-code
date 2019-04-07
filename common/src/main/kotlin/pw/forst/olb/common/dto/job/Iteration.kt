package pw.forst.olb.common.dto.job

interface Iteration : Comparable<Iteration> {

    val position: Int

    override fun compareTo(other: Iteration): Int = this.position.compareTo(other.position)

    operator fun plus(other: Iteration): Iteration
}
