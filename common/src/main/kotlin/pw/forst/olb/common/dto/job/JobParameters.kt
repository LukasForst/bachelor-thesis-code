package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import java.io.Serializable

interface JobParameters : Serializable {
    val maxTime: Time

    val maxCost: Cost

    val jobType: JobType
}
