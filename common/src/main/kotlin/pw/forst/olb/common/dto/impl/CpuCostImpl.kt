package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.resources.CpuCost
import pw.forst.olb.common.dto.resources.CpuResources

data class CpuCostImpl(
    override val value: Double
) : CpuCost {

    override fun plus(other: CpuCost): CpuCost = copy(value = value + other.value)

    override fun plus(other: Cost): Cost = createCost(value = value + other.value)

    override fun minus(other: CpuCost): CpuCost = copy(value = value - other.value)

    override fun minus(other: Cost): Cost = createCost(value = value + other.value)

    override fun times(other: CpuResources): Cost = createCost(value = value * other.cpuValue)

    override fun times(other: Int): Cost = createCost(other * value)

    override fun times(other: Long): Cost = createCost(other * value)

    override fun times(other: Double): Cost = createCost(other * value)

    override fun compareTo(other: Cost): Int = value.compareTo(other.value)
}
