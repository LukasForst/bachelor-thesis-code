package pw.forst.olb.common.dto

interface SchedulingProperties {
    val startTime: Time

    val endTime: Time

    val timeStep: Time

    val maxTimePlanningSpend: Time

    val cores: Int?
}
