package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time

interface JobParameters {
    val maxTime: Time

    val maxCost: Cost

    val jobType: JobType
}
