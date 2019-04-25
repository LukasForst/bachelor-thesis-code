package pw.forst.olb.common.dto.job

import java.io.Serializable

interface Iteration : Comparable<Iteration>, Serializable {

    val position: Int

    override fun compareTo(other: Iteration): Int = this.position.compareTo(other.position)

    operator fun plus(other: Iteration): Iteration
}
