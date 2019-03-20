package pw.forst.olb.simulation.prediction

data class Input(
    val index: Long,
    val timeRecord: Long,
    val iterationLength: Int,
    val cost: Double
) : Comparable<Input> {
    override fun compareTo(other: Input): Int = this.index.compareTo(other.index)

}
