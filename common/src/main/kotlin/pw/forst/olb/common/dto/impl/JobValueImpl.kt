package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.job.JobValue

data class JobValueImpl(override val value: Double) : JobValue {
    init {
        assert(value != Double.NaN)
    }

    override fun plus(other: JobValue): JobValue = copy(value = this.value + other.value)

    override fun minus(other: JobValue): JobValue = copy(value = this.value - other.value)
}
