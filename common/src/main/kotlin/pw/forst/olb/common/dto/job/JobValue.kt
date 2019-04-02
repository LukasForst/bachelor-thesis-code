package pw.forst.olb.common.dto.job

interface JobValue {
    val value: Double

    operator fun plus(other: JobValue): JobValue

    operator fun minus(other: JobValue): JobValue
}
