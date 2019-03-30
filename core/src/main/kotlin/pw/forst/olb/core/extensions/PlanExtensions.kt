package pw.forst.olb.core.extensions

import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.dto.jobPlanView
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.evaluation.CompletePlan

fun Plan.isConvertible(): Boolean = startTime != null && endTime != null && timeIncrement != null && cost != null

fun Plan.asCompletePlan(): CompletePlan? =
    if (isConvertible())
        CompletePlan(
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


fun Plan.toJobPlanViews(): Collection<JobPlanView> = createJobPlanViews(assignments.mapNotNull { it.toCompleteAssignment() }, jobDomain)

fun CompletePlan.toJobPlanViews(): Collection<JobPlanView> = createJobPlanViews(assignments, jobDomain)

internal fun createJobPlanViews(assignments: Collection<CompleteJobAssignment>, jobDomain: Collection<Job>): Collection<JobPlanView> {
    val jobs = assignments.groupBy { it.job }
    return jobDomain.map { jobPlanView(it, jobs[it] ?: emptyList()) }
}
