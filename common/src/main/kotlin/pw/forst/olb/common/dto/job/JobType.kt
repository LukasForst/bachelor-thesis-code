package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.resources.CpuPowerType

enum class JobType {
    PARALELIZED,
    SINGLE_CORE_HEAVY
}

fun JobType.toCpuResources(): CpuPowerType = when (this) {
    JobType.PARALELIZED -> CpuPowerType.MULTI_CORE
    JobType.SINGLE_CORE_HEAVY -> CpuPowerType.SINGLE_CORE
}
