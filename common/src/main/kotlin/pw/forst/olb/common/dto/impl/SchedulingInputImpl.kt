package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool

data class SchedulingInputImpl(
    override val resources: Collection<ResourcesPool>,
    override val jobs: Collection<Job>,
    override val startTime: Time,
    override val endTime: Time,
    override val timeStep: Time,
    override val maxTimePlanningSpend: Time,
    override val cores: Int?
) : SchedulingInput
