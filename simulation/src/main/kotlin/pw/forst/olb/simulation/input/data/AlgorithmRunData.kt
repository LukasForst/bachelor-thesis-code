package pw.forst.olb.simulation.input.data

import pw.forst.olb.common.dto.Time

data class AlgorithmRuntimeInfo(
    val name: String,
    val data: Collection<AlgorithmRunData>
)

data class AlgorithmRunData(
    val index: Int,
    val timePoint: Time,
    val iterationLength: Int,
    val cost: Double
)
