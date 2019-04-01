package pw.forst.olb.core.api

import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties

class OlbCoreApiImpl : OlbCoreApi {

    override fun createNewPlan(input: SchedulingInput): AllocationPlan {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun enhancePlan(plan: AllocationPlanWithHistory, properties: SchedulingProperties): AllocationPlan {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
