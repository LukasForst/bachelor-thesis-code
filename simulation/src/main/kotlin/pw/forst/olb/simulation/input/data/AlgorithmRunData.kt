package pw.forst.olb.simulation.input.data

data class AlgorithmRuntimeInfo(
    val name: String,
    val data: Collection<AlgorithmRunData>
)

data class AlgorithmRunData(
    val index: Int,
    val timePoint: Int,
    val iterationLength: Int,
    val cost: Double
)
