package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.resources.ResourcesProvider

data class ResourcesPoolImpl(
    override val provider: ResourcesProvider,
    override val cpuResources: CpuResources, override val memoryResources: MemoryResources

) : ResourcesPool {
    override fun minus(other: MemoryResources): ResourcesPool = copy(memoryResources = this.memoryResources - other)

    override fun minus(other: CpuResources): ResourcesPool = copy(cpuResources = this.cpuResources - other)
}
