package pw.forst.olb.common.dto

import java.io.Serializable

interface SchedulingProperties : Serializable {
    val startTime: Time

    val endTime: Time

    val timeStep: Time

    val maxTimePlanningSpend: Time

    val cores: Int?
}
