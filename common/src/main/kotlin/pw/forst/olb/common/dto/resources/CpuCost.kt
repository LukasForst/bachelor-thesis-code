package pw.forst.olb.common.dto.resources

import pw.forst.olb.common.dto.Cost

interface CpuCost : Cost {

    operator fun plus(other: CpuCost): CpuCost

    operator fun minus(other: CpuCost): CpuCost

    operator fun times(other: CpuResources): Cost
}
