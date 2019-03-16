package pw.forst.olb.core.nw

import pw.forst.olb.common.dto.scheduling.PlanView
import pw.forst.olb.common.dto.scheduling.SchedulingInputData

interface Scheduler {
    fun run(input: SchedulingInputData): PlanView
}
