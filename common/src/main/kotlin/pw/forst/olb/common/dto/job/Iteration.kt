package pw.forst.olb.common.dto.job

interface Iteration : Comparable<Iteration> {

    val position: Long

    val iterationLengthInMls: Int

    override fun compareTo(other: Iteration): Int = this.position.compareTo(other.position)
}
