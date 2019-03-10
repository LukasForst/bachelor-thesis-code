package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesStackInfo

data class JobAssignment(
    val job: Job,
    val time: Time,
    val cpu: CpuResources,
    val memory: MemoryResources,
    val resourcesStack: ResourcesStackInfo
)
