package pw.forst.olb.common.dto.resources

import java.io.Serializable

sealed class Resources : Serializable

data class CpuResources(
    /**
     * 2.5 represents two wholes cores and half of the power of one core
     * */
    val cpuValue: Double,

    val type: CpuPowerType
) : Resources(), Comparable<CpuResources>, Serializable {

    companion object {

        fun getSmallest(powerType: CpuPowerType) = CpuResources(1.0, powerType)

        fun getZero(powerType: CpuPowerType): CpuResources = CpuResources(0.0, powerType)
    }

    operator fun plus(other: CpuResources) = assertSameType(other) { copy(cpuValue = this.cpuValue + other.cpuValue) }

    operator fun minus(other: CpuResources) = assertSameType(other) { copy(cpuValue = this.cpuValue - other.cpuValue) }

    override fun compareTo(other: CpuResources): Int = this.cpuValue.compareTo(other.cpuValue)

    private fun <T> assertSameType(other: CpuResources, block: () -> T): T =
        if (this.type != other.type) throw IllegalArgumentException("It is not possible to operate with different CPU type! this: [$this], other: [$other]")
        else block()
}

enum class CpuPowerType {
    SINGLE_CORE,
    MULTI_CORE
}

data class MemoryResources(

    val memoryInMegaBytes: Long

) : Resources(), Comparable<MemoryResources>, Serializable {

    companion object {
        fun getSmallest() = MemoryResources(128L)

        val ZERO: MemoryResources
            get() = MemoryResources(0L)
    }

    operator fun times(other: Int) = copy(memoryInMegaBytes = memoryInMegaBytes * other)

    operator fun plus(other: MemoryResources) = copy(memoryInMegaBytes = this.memoryInMegaBytes + other.memoryInMegaBytes)

    operator fun minus(other: MemoryResources) = copy(memoryInMegaBytes = this.memoryInMegaBytes - other.memoryInMegaBytes)

    override fun compareTo(other: MemoryResources): Int = this.memoryInMegaBytes.compareTo(other.memoryInMegaBytes)
}
