package pw.forst.olb.core.constraints.penalty

data class PenaltyImpl(
    override val hard: Double,
    override val soft: Double
) : Penalty {

    override fun plus(other: Penalty): Penalty = copy(hard = this.hard + other.hard, soft = this.soft + other.soft)

    override fun minus(other: Penalty): Penalty = copy(hard = this.hard - other.hard, soft = this.soft - other.soft)

    override fun times(other: Int): Penalty = copy(hard = this.hard * other, soft = this.soft * other)

    override fun times(other: Long): Penalty = copy(hard = this.hard * other, soft = this.soft * other)

    override fun times(other: Double): Penalty = copy(hard = this.hard * other, soft = this.soft * other)

}
