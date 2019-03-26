package pw.forst.olb.common.dto

interface Cost : Comparable<Cost> {
    val value: Double

    operator fun plus(other: Cost): Cost

    operator fun minus(other: Cost): Cost

    operator fun times(other: Int): Cost

    operator fun times(other: Long): Cost

    operator fun times(other: Double): Cost
}

fun invalidCost(): Cost = CostImpl(Double.MAX_VALUE)

fun createCost(cost: Cost): Cost = CostImpl(cost.value)

fun createCost(value: Double): Cost = CostImpl(value)

data class CostImpl constructor(override val value: Double) : Cost {

    override fun times(other: Int): Cost = copy(value = this.value * other)

    override fun times(other: Long): Cost = copy(value = this.value * other)

    override fun times(other: Double): Cost = copy(value = this.value * other)

    override fun compareTo(other: Cost): Int = this.value.compareTo(other.value)

    override operator fun plus(other: Cost): Cost = copy(value = this.value + other.value)

    override operator fun minus(other: Cost): Cost = copy(value = this.value - other.value)
}


fun Collection<Cost>.sum(): Cost = this.fold(createCost(0.0)) { r, t -> r + t }

fun Collection<Cost>.sumOnlyValues(): Cost = createCost(this.sumByDouble { it.value })

