package pw.forst.olb.core.constraints.moves

import mu.KLogging
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter
import org.optaplanner.core.impl.score.director.ScoreDirector
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.domain.PlanJobAssignment
import pw.forst.olb.core.extensions.isInPlanningWindow

class ResourcesSelectionFilter : SelectionFilter<Plan, PlanJobAssignment> {

    private companion object : KLogging()

    override fun accept(scoreDirector: ScoreDirector<Plan>, selection: PlanJobAssignment): Boolean = accept(scoreDirector.workingSolution, selection)

    private fun accept(plan: Plan, selection: PlanJobAssignment): Boolean {
        if (!selection.isValid) return true

        if (!selection.isMovable) return false

        // if is not in planning window, this assignment must stay where it is
        if (!selection.isInPlanningWindow(plan)) return false

        val selectionProvider = selection.allocation!!.provider
        val selectionTime = selection.time
        val selectionJob = selection.job!!

        val jobFiltered = plan.assignments.filter { it.job == selectionJob }

        // it is not possible to use different allocation provider at one time
        if (jobFiltered.any { it.time == selectionTime && it.allocation?.provider != selectionProvider }) return false

        val previousTime = selectionTime - plan.timeIncrement
        val nextTime = selectionTime + plan.timeIncrement
        // it is not possible to use different allocation provider between times, there must be turn off

        if (jobFiltered.any { it.allocation?.provider != selectionProvider && (it.time == previousTime || it.time == nextTime) }) return false

        return true
    }

}
