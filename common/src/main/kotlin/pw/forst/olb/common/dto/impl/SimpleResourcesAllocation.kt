package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesProvider

data class SimpleResourcesAllocation(

    override val provider: ResourcesProvider,

    override val cpuResources: CpuResources,

    override val memoryResources: MemoryResources

) : ResourcesAllocation {

    override val cost: Cost by lazy { provider * cpuResources + provider * memoryResources }

    override fun plus(other: MemoryResources): ResourcesAllocation = copy(memoryResources = memoryResources + other)

    override fun minus(other: MemoryResources): ResourcesAllocation = copy(memoryResources = memoryResources - other)

    override fun plus(other: CpuResources): ResourcesAllocation = copy(cpuResources = cpuResources + other)

    override fun minus(other: CpuResources): ResourcesAllocation = copy(cpuResources = cpuResources - other)
}
