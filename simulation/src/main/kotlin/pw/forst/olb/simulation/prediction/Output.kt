package pw.forst.olb.simulation.prediction

data class Output(
    val index: Long,
    val timeRecord: Long,
    val iterationLength: Int,
    val cost: Double,

    val costPredictions: List<Double>
)
