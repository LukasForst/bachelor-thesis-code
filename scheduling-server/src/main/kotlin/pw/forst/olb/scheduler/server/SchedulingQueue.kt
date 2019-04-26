package pw.forst.olb.scheduler.server

import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.server.NewPlanCreationRequest
import pw.forst.olb.common.dto.server.PlanEnhancementRequest
import java.util.concurrent.ExecutorService

class SchedulingQueue(
    private val olbCoreApi: OlbCoreApi,
    private val executorService: ExecutorService
) {

    fun execute(request: NewPlanCreationRequest, callBack: (AllocationPlan) -> Unit) {
        executorService.execute { olbCoreApi.createNewPlan(request.input).let(callBack) }
    }

    fun execute(request: PlanEnhancementRequest, callBack: (AllocationPlan) -> Unit) {
        executorService.execute { olbCoreApi.enhancePlan(request.plan, request.properties).let(callBack) }
    }
}
