package pw.forst.olb.common.dto.scheduling

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import java.util.SortedMap

interface PlanView {

    val start: Time

    val end: Time

    val granularity: Time

    /**
     * In the background it should be [SortedMap]
     * */
    val timeViews: Map<Time, TimePlanView>

    /**
     * Cost of all resources used in this plan view
     * */
    val resourcesCost: Cost
}


