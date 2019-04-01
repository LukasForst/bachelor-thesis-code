package pw.forst.olb.common.dto.resources

import pw.forst.olb.common.dto.Cost

interface ResourcesAllocation : ResourcesPool {

    /**
     * Allocation cost -> memory + cpu
     * */
    val cost: Cost

    operator fun plus(other: ResourcesAllocation): ResourcesAllocation

    operator fun minus(other: ResourcesAllocation): ResourcesAllocation

    operator fun plus(other: MemoryResources): ResourcesAllocation

    override operator fun minus(other: MemoryResources): ResourcesAllocation

    operator fun plus(other: CpuResources): ResourcesAllocation

    override operator fun minus(other: CpuResources): ResourcesAllocation
}
