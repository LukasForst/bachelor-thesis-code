package pw.forst.olb.core

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.planning.Plan

interface Evaluator {
    fun evaluate(plan: Plan): Cost

}
