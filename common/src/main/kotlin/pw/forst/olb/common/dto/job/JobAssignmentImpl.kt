package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesStackInfo

data class JobAssignmentImpl(
    val job: Job,
    val time: Time,
    val cpu: CpuResources,
    val memory: MemoryResources,
    val resourcesStack: ResourcesStackInfo
) {
    val cost by lazy { resourcesStack.cpuCost * cpu + resourcesStack.memoryCost * memory }
}

interface JobAssignment {

    val job: Job

    val time: Time

    val allocation: ResourcesAllocation

    val cost: Cost
}
