package pw.forst.olb.common.dto.resources

import pw.forst.olb.common.dto.Cost
import java.util.UUID

interface ResourcesProvider {
    val name: String

    val uuid: UUID

    val cpuCost: CpuCost

    val memoryCost: MemoryCost

    val initialPool: ResourcesPool

    operator fun times(other: CpuResources) = cpuCost * other.cpuValue

    operator fun times(other: MemoryResources) = memoryCost * other.memoryInMegaBytes
}

interface ResourcesPool {

    val provider: ResourcesProvider

    val cpuResources: CpuResources

    val memoryResources: MemoryResources

    operator fun minus(other: MemoryResources): ResourcesPool

    operator fun minus(other: CpuResources): ResourcesPool

}

interface ResourcesAllocation : ResourcesPool {

    /**
     * Allocation cost -> memory + cpu
     * */
    val cost: Cost

    operator fun plus(other: MemoryResources): ResourcesAllocation

    override operator fun minus(other: MemoryResources): ResourcesAllocation

    operator fun plus(other: CpuResources): ResourcesAllocation

    override operator fun minus(other: CpuResources): ResourcesAllocation
}

//data class ResourcesProvider(
//    override val name: String,
//    override val cpuCost: CpuCost,
//    override val memoryCost: MemoryCost,
//    override val uuid: UUID = UUID.randomUUID()
//
//) : ResourcesStackInfo
//
//data class ResourcesPool(
//    val provider: ResourcesProvider,
//    val cpuResources: CpuResources,
//    val memoryResources: MemoryResources
//
//) {
//    operator fun plus(other: MemoryResources) = copy(memoryResources = this.memoryResources + other)
//    operator fun minus(other: MemoryResources) = copy(memoryResources = this.memoryResources - other)
//
//    operator fun plus(other: CpuResources) = copy(cpuResources = this.cpuResources + other)
//    operator fun minus(other: CpuResources) = copy(cpuResources = this.cpuResources - other)
//}
//
//data class ResourcesAllocation(
//    val provider: ResourcesProvider,
//
//    )
