package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobType

data class SimpleJobParameters(
    override val maxTime: Time,
    override val maxCost: Cost,
    override val jobType: JobType
) : JobParameters
