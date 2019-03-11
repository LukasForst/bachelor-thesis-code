package pw.forst.olb.core

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.planning.SimplePlan

interface Evaluator {
    fun evaluate(plan: SimplePlan): Cost

}
