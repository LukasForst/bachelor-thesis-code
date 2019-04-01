package pw.forst.olb.common.dto.resources

sealed class Resources

data class CpuResources(
    /**
     * 2.5 represents two wholes cores and half of the power of one core
     * */
    val cpuValue: Double,

    val type: CpuPowerType
) : Resources() {

    companion object {
        val ZERO_MULT: CpuResources
            get() = CpuResources(0.0, CpuPowerType.MULTI_CORE)

        val ZERO_SING: CpuResources
            get() = CpuResources(0.0, CpuPowerType.SINGLE_CORE)

        fun getZero(powerType: CpuPowerType): CpuResources = when (powerType) {
            CpuPowerType.SINGLE_CORE -> ZERO_SING
            CpuPowerType.MULTI_CORE -> ZERO_MULT
        }
    }

    operator fun plus(other: CpuResources) = assertSameType(other) { copy(cpuValue = this.cpuValue + other.cpuValue) }

    operator fun minus(other: CpuResources) = assertSameType(other) { copy(cpuValue = this.cpuValue - other.cpuValue) }


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

) : Resources() {

    companion object {
        val ZERO: MemoryResources
            get() = MemoryResources(0L)
    }

    operator fun plus(other: MemoryResources) = copy(memoryInMegaBytes = this.memoryInMegaBytes + other.memoryInMegaBytes)

    operator fun minus(other: MemoryResources) = copy(memoryInMegaBytes = this.memoryInMegaBytes - other.memoryInMegaBytes)
}
