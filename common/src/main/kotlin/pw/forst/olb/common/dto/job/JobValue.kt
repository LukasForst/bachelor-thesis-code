package pw.forst.olb.common.dto.job

import java.io.Serializable

interface JobValue : Serializable {
    val value: Double

    operator fun plus(other: JobValue): JobValue

    operator fun minus(other: JobValue): JobValue
}
