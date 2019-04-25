package pw.forst.olb.common.dto.resources

import java.io.Serializable
import java.util.UUID

interface ResourcesProvider : Serializable {
    val name: String

    val uuid: UUID

    val cpuCost: CpuCost

    val memoryCost: MemoryCost

    operator fun times(other: CpuResources) = cpuCost * other.cpuValue

    operator fun times(other: MemoryResources) = memoryCost * other.memoryInMegaBytes
}
