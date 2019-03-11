package pw.forst.olb.common.dto.resources

import java.util.UUID

interface ResourcesStackInfo {
    val name: String

    val uuid: UUID

    val cpuCost: CpuCost

    val memoryCost: MemoryCost

    operator fun times(other: CpuResources) = cpuCost * other.cpuValue
    operator fun times(other: MemoryResources) = memoryCost * other.memoryInMegaBytes
}


data class ResourcesStack(
    override val name: String,

    val memoryResources: MemoryResources,
    override val memoryCost: MemoryCost,

    val cpuResources: CpuResources,
    override val cpuCost: CpuCost,

    override val uuid: UUID = UUID.randomUUID()
) : ResourcesStackInfo {
    operator fun plus(other: MemoryResources) = copy(memoryResources = this.memoryResources + other)
    operator fun minus(other: MemoryResources) = copy(memoryResources = this.memoryResources - other)

    operator fun plus(other: CpuResources) = copy(cpuResources = this.cpuResources + other)
    operator fun minus(other: CpuResources) = copy(cpuResources = this.cpuResources - other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResourcesStack

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}

infix fun ResourcesStackInfo.isFor(stack: ResourcesStackInfo) = this.uuid == stack.uuid
