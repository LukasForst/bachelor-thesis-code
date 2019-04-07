package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.Time

data class SchedulingPropertiesImpl(
    override val startTime: Time,
    override val endTime: Time,
    override val timeStep: Time,
    override val maxTimePlanningSpend: Time,
    override val cores: Int?
) : SchedulingProperties
