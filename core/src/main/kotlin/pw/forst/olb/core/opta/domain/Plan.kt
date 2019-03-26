package pw.forst.olb.core.opta.domain

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.simple.SimpleScore

@PlanningSolution
data class Plan(
    val id: String = "",

    @field:PlanningEntityCollectionProperty
    var assignments: List<Assignment> = emptyList(),

    @field:ValueRangeProvider(id = "jobRange")
    @field:ProblemFactCollectionProperty
    var jobDomain: List<Job> = emptyList(),

    @field:ValueRangeProvider(id = "resourcesRange")
    @field:ProblemFactCollectionProperty
    var resourcesStackDomain: List<ResourcesStack> = emptyList(),

    @field:ValueRangeProvider(id = "timeRange")
    @field:ProblemFactCollectionProperty
    var times: List<Time> = emptyList(),

    @PlanningScore
    var cost: SimpleScore? = null
)

fun Plan.getPure(): Plan = copy(
    assignments = assignments.filter { it.isValid }
)

fun Plan.getTimeLengthForJob(job: Job): Long {

    val (min, max) = this.assignments
        .filter { it.job == job }
        .filter { it.isValid }
        .let { coll -> coll.minBy { it.time!! }?.time to coll.maxBy { it.time!! }?.time }

    return if (min == null || max == null) 0L else max.position - min.position
}

