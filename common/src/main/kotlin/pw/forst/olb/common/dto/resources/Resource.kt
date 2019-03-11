package pw.forst.olb.common.dto.resources

sealed class Resources

data class CpuResources(
    /**
     * 2.5 represents two wholes cores and half of the power of one core
     * */
    val cpuValue: Double,

    val type: CpuPowerType
) : Resources() {
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
    operator fun plus(other: MemoryResources) = copy(memoryInMegaBytes = this.memoryInMegaBytes + other.memoryInMegaBytes)

    operator fun minus(other: MemoryResources) = copy(memoryInMegaBytes = this.memoryInMegaBytes - other.memoryInMegaBytes)
}
