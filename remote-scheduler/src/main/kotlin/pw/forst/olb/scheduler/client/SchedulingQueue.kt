package pw.forst.olb.scheduler.client

import mu.KLogging
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.server.NewPlanCreationRequest
import pw.forst.olb.common.dto.server.PlanEnhancementRequest
import pw.forst.olb.common.dto.server.ReportEndpoint
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class SchedulingQueue(
    private val requestSender: RequestSender,
    private val reportEndpointBase: String
) {

    private companion object : KLogging() {
        var idCounter: Int = 0
    }

    private val queue: BlockingQueue<SchedulingResponse> = LinkedBlockingQueue()

    fun execute(input: SchedulingInput): AllocationPlan {
        val schedulingId = ++idCounter
        val request = NewPlanCreationRequest(
            input = input,
            reportEndpoint = ReportEndpoint("POST", "$reportEndpointBase/$schedulingId")
        )
        requestSender.handle(request)
        return waitForId(schedulingId)
    }

    fun execute(plan: AllocationPlanWithHistory, properties: SchedulingProperties): AllocationPlan {
        val schedulingId = ++idCounter
        val request = PlanEnhancementRequest(
            plan = plan,
            properties = properties,
            reportEndpoint = ReportEndpoint("POST", "$reportEndpointBase/$schedulingId")
        )
        requestSender.handle(request)
        return waitForId(schedulingId)
    }

    private fun waitForId(schedulingId: Int): AllocationPlan {
        val checkCondition = { response: SchedulingResponse -> response.id == schedulingId && response.plan != null }
        var response: SchedulingResponse
        do {
            logger.info { "Checking queue, waiting for id $schedulingId" }
            response = queue.take()
            if (!checkCondition(response)) {
                queue.put(response)
                logger.info { "There is ${response.id} plan, waiting for $schedulingId" }
            }
        } while (!checkCondition(response))

        logger.info { "Plan with Id $schedulingId retrieved!" }
        return response.plan!!
    }

    fun receive(id: Int, plan: AllocationPlan) {
        logger.info { "Putting plan $id to the queue" }
        queue.put(SchedulingResponse(id, plan))
        logger.info { "Plan $id enqueued" }
    }

    private data class SchedulingResponse(val id: Int, val plan: AllocationPlan? = null)
}
