package pw.forst.olb.common.dto.server

import pw.forst.olb.common.dto.AllocationPlan

data class ServerResponse(
    val reportEndpoint: ReportEndpoint,
    val allocationPlan: AllocationPlan
)
