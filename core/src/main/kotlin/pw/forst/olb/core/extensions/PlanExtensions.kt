package pw.forst.olb.core.extensions

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.dto.JobPlanViewImpl
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.evaluation.CompletePlan

fun Plan.isConvertible(): Boolean = startTime != null && endTime != null && timeIncrement != null && cost != null

fun Plan.asCompletePlan(): CompletePlan? =
    if (isConvertible())
        CompletePlan(
            uuid = uuid,
            startTime = startTime!!,
            endTime = endTime!!,
            timeIncrement = timeIncrement!!,
            assignments = assignments.mapNotNull { it.toCompleteAssignment() },
            jobDomain = jobDomain,
            resourcesStackDomain = resourcesStackDomain,
            times = times,
            cost = cost!!
        )
    else null


fun Plan.toJobPlanViews(): Collection<JobPlanView> = createJobPlanViews(assignments.mapNotNull { it.toCompleteAssignment() }, jobDomain, this)

fun CompletePlan.toJobPlanViews(): Collection<JobPlanView> = createJobPlanViews(assignments, jobDomain, this)

internal fun createJobPlanViews(assignments: Collection<CompleteJobAssignment>, jobDomain: Collection<Job>, plan: GenericPlan): Collection<JobPlanView> {
    val jobs = assignments.groupBy { it.job }
    return jobDomain.map { JobPlanViewImpl(it, jobs[it] ?: emptyList(), plan) }
}
