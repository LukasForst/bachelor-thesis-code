package pw.forst.olb.core.api

import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties

interface OlbCoreApi {

    fun createNewPlan(input: SchedulingInput): AllocationPlan

    fun enhancePlan(plan: AllocationPlanWithHistory, properties: SchedulingProperties): AllocationPlan

}
