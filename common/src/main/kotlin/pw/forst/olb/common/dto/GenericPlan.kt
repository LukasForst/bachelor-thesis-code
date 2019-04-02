package pw.forst.olb.common.dto

import java.util.UUID

interface GenericPlan {
    val uuid: UUID

    val startTime: Time

    val endTime: Time

    val timeIncrement: Time

}
