package pw.forst.olb.common.dto.server

import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.SchedulingProperties

data class PlanEnhancementRequest(
    val plan: AllocationPlanWithHistory,
    val properties: SchedulingProperties,
    override val reportEndpoint: ReportEndpoint
) : SchedulingRequest
