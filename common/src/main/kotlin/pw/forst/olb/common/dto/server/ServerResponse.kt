package pw.forst.olb.common.dto.server

import pw.forst.olb.common.dto.AllocationPlan
import java.io.Serializable

data class ServerResponse(
    val reportEndpoint: ReportEndpoint,
    val allocationPlan: AllocationPlan
) : Serializable
