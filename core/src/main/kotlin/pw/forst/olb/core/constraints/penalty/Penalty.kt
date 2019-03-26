package pw.forst.olb.core.constraints.penalty

interface Penalty {
    val hard: Double

    val soft: Double

    operator fun plus(other: Penalty): Penalty

    operator fun minus(other: Penalty): Penalty

    operator fun times(other: Int): Penalty

    operator fun times(other: Long): Penalty

    operator fun times(other: Double): Penalty

}
