package pw.forst.olb.core.constraints.filter

import mu.KLogging
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.domain.PlanJobAssignment

class ResourcesSelectionFilter : SelectionFilter<Plan, PlanJobAssignment> {

    private companion object : KLogging()

    override fun accept(scoreDirector: ScoreDirector<Plan>, selection: PlanJobAssignment): Boolean = accept(scoreDirector.workingSolution, selection)

    private fun accept(plan: Plan, selection: PlanJobAssignment): Boolean {
        if (!selection.isValid) return true

        val selectionProvider = selection.allocation!!.provider
        val selectionTime = selection.time
        val selectionJob = selection.job!!

        val jobFiltered = plan.assignments.filter { it.job == selectionJob }

        // it is not possible to use different allocation provider at one time
        if (jobFiltered.any { it.time == selectionTime && it.allocation?.provider != selectionProvider }) return false

        val previousTime = selectionTime - plan.timeIncrement.also { if (it.position < 0) return true }

        // it is not possible to use different allocation provider between times, there must be turn off
        if (jobFiltered.any { it.time == previousTime && it.allocation?.provider != selectionProvider }) return false

        return true
    }

}
