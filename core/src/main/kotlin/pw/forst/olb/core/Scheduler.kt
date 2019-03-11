package pw.forst.olb.core

import pw.forst.olb.common.dto.planning.PlanningInput
import pw.forst.olb.common.dto.planning.SimplePlan

/**
 * Scheduler interface should be implemented by every planning executor
 * */
interface Scheduler {
    /**
     * Executes planning for specified input
     * */
    fun createPlan(input: PlanningInput): SimplePlan
}
