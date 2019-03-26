package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.resources.MemoryCost
import pw.forst.olb.common.dto.resources.MemoryResources

data class MemoryCostImpl(override val value: Double) : MemoryCost {

    override fun plus(other: MemoryCost): MemoryCost = copy(value = value + other.value)

    override fun plus(other: Cost): Cost = createCost(value = value + other.value)

    override fun minus(other: MemoryCost): MemoryCost = copy(value = value - other.value)

    override fun minus(other: Cost): Cost = createCost(value = value + other.value)

    override fun times(other: MemoryResources): Cost = createCost(value = value * other.memoryInMegaBytes)

    override fun times(other: Int): Cost = createCost(other * value)

    override fun times(other: Long): Cost = createCost(other * value)

    override fun times(other: Double): Cost = createCost(other * value)

    override fun compareTo(other: Cost): Int = value.compareTo(other.value)
}
