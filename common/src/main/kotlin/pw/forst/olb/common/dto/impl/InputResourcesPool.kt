package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.resources.CpuCost
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryCost
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.resources.ResourcesProvider
import java.util.UUID

data class InputResourcesPool(
    override val name: String,
    override val uuid: UUID,
    override val cpuCost: CpuCost,
    override val memoryCost: MemoryCost,
    override val cpuResources: CpuResources,
    override val memoryResources: MemoryResources
) : ResourcesPool, ResourcesProvider {

    override val provider: ResourcesProvider by lazy { this }

    override fun minus(other: MemoryResources): ResourcesPool = copy(memoryResources = memoryResources - other)

    override fun minus(other: CpuResources): ResourcesPool = copy(cpuResources = cpuResources - other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputResourcesPool

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }


}
