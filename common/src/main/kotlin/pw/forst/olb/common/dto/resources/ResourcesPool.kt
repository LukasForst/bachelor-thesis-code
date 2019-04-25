package pw.forst.olb.common.dto.resources

import java.io.Serializable

interface ResourcesPool : Serializable {

    val provider: ResourcesProvider

    val cpuResources: CpuResources

    val memoryResources: MemoryResources

    operator fun minus(other: MemoryResources): ResourcesPool

    operator fun minus(other: CpuResources): ResourcesPool

}
