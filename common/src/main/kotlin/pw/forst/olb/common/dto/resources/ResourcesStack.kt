package pw.forst.olb.common.dto.resources

import java.util.UUID

data class ResourcesStack(
    val name: String,

    val memoryResources: MemoryResources,
    val memoryCost: MemoryCost,

    val cpuResources: CpuResources,
    val cpuCost: CpuCost,

    val uuid: UUID = UUID.randomUUID()
) {
    operator fun plus(other: MemoryResources) = copy(memoryResources = this.memoryResources + other)
    operator fun minus(other: MemoryResources) = copy(memoryResources = this.memoryResources - other)

    operator fun plus(other: CpuResources) = copy(cpuResources = this.cpuResources + other)
    operator fun minus(other: CpuResources) = copy(cpuResources = this.cpuResources - other)
}
