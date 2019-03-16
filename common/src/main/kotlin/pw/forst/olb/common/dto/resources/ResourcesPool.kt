package pw.forst.olb.common.dto.resources

interface ResourcesPool {

    val provider: ResourcesProvider

    val cpuResources: CpuResources

    val memoryResources: MemoryResources

    operator fun minus(other: MemoryResources): ResourcesPool

    operator fun minus(other: CpuResources): ResourcesPool

}
