package pw.forst.olb.core

import pw.forst.olb.common.dto.planning.Plan
import pw.forst.olb.common.dto.planning.PlanningInput

/**
 * Scheduler interface should be implemented by every planning executor
 * */
interface Scheduler {
    /**
     * Executes planning for specified input
     * */
    fun createPlan(input: PlanningInput): Plan
}
