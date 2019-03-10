package pw.forst.olb.core.naive

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.planning.Plan
import pw.forst.olb.common.dto.sumOnlyValues
import pw.forst.olb.core.Evaluator


class NaiveEvaluator : Evaluator {

    override fun evaluate(plan: Plan): Cost =
        plan.assignments.values
            .flatMap { assignments ->
                assignments.map { assignment -> assignment.resourcesStack.cpuCost * assignment.cpu + assignment.resourcesStack.memoryCost * assignment.memory }
            }.sumOnlyValues()
}
