package pw.forst.olb.common.dto.resources

import pw.forst.olb.common.dto.Cost

interface MemoryCost : Cost {

    operator fun plus(other: MemoryCost): MemoryCost

    operator fun minus(other: MemoryCost): MemoryCost

    operator fun times(other: MemoryResources): Cost
}

