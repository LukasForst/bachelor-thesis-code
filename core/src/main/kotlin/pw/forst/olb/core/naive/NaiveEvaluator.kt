package pw.forst.olb.core.naive

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.planning.SimplePlan
import pw.forst.olb.common.dto.sumOnlyValues
import pw.forst.olb.core.Evaluator


class NaiveEvaluator : Evaluator {

    override fun evaluate(plan: SimplePlan): Cost =
        plan.assignments.values
            .flatMap { assignments ->
                assignments.map { (_, assignment) -> assignment.resourcesStack.cpuCost * assignment.cpu + assignment.resourcesStack.memoryCost * assignment.memory }
            }.sumOnlyValues()
}
