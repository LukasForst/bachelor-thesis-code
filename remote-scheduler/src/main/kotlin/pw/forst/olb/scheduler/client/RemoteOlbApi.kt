package pw.forst.olb.scheduler.client

import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties

class RemoteOlbApi(private val schedulingQueue: SchedulingQueue) : OlbCoreApi {

    override fun createNewPlan(input: SchedulingInput): AllocationPlan = schedulingQueue.execute(input)

    override fun enhancePlan(plan: AllocationPlanWithHistory, properties: SchedulingProperties): AllocationPlan = schedulingQueue.execute(plan, properties)
}
