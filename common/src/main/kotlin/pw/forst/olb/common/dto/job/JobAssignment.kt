package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesStack

data class JobAssignment(
    val job: Job,
    val time: Time,
    val cpu: CpuResources,
    val memory: MemoryResources,
    val resourcesStack: ResourcesStack
) {
    init {
        if ((resourcesStack.cpuResources - cpu).cpusPercentage < 0)
            throw IllegalArgumentException("It is not possible to create assignment where cpu percentage would be negative!")
        if ((resourcesStack.memoryResources - memory).memoryInMegaBytes < 0)
            throw IllegalArgumentException("It is not possible to create assignment where memory data would be negative!")
    }

}

