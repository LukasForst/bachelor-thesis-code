package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.job.JobValue

data class JobValueImpl(override val value: Double) : JobValue {
    init {
        assert(value != Double.NaN)
    }
}
