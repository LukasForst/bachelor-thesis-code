package pw.forst.olb.common.dto

import java.io.Serializable
import java.util.UUID

interface GenericPlan : Serializable {
    val uuid: UUID

    val startTime: Time

    val endTime: Time

    val timeIncrement: Time
}
