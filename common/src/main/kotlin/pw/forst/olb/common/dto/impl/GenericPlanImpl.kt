package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import java.util.UUID

data class GenericPlanImpl(override val uuid: UUID, override val startTime: Time, override val endTime: Time, override val timeIncrement: Time) : GenericPlan
