package pw.forst.olb.common.dto.resources

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.createCost

data class MemoryCost(
    /**
     * Generally cost per megabyte of RAM
     * */
    val cost: Cost
) : Cost by cost {

    operator fun plus(other: MemoryCost): MemoryCost = copy(cost = this.cost + other)

    operator fun minus(other: MemoryCost): MemoryCost = copy(cost = this.cost + other)

    operator fun times(other: MemoryResources): Cost = createCost(cost = this.cost * other.memoryInMegaBytes)
}

data class CpuCost(

    /**
     * Generally cost per core
     * */
    val cost: Cost
) : Cost by cost {

    operator fun plus(other: CpuCost): CpuCost = copy(cost = this.cost + other)

    operator fun minus(other: CpuCost): CpuCost = copy(cost = this.cost + other)

    operator fun times(other: CpuResources): Cost = createCost(cost = this.cost * other.cpusPercentage)
}
