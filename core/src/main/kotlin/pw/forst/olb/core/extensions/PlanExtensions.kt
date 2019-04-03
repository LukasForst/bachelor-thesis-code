package pw.forst.olb.core.extensions

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.dto.JobPlanViewImpl
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.evaluation.CompletePlan

fun Plan.asCompletePlan(): CompletePlan = CompletePlan(
    uuid = uuid,
    startTime = startTime,
    endTime = endTime,
    timeIncrement = timeIncrement,
    assignments = assignments.mapNotNull { it.toCompleteAssignment() },
    jobDomain = jobDomain,
    resourcesStackDomain = resourcesStackDomain,
    times = times,
    cost = cost
)


fun Plan.toJobPlanViews(): Collection<JobPlanView> = createJobPlanViews(assignments.mapNotNull { it.toCompleteAssignment() }, jobDomain, this)

fun Plan.toJobPlanViewsInPlanningWindow(): Collection<JobPlanView> = createJobPlanViews(
    assignments.mapNotNull { if (it.isInPlanningWindow(this)) it.toCompleteAssignment() else null },
    jobDomain,
    this
)

fun CompletePlan.toJobPlanViews(): Collection<JobPlanView> = createJobPlanViews(assignments, jobDomain, this)

internal fun createJobPlanViews(assignments: Collection<CompleteJobAssignment>, jobDomain: Collection<Job>, plan: GenericPlan): Collection<JobPlanView> {
    val jobs = assignments.groupBy { it.job }
    return jobDomain.map { JobPlanViewImpl(it, jobs[it] ?: emptyList(), plan) }
}
