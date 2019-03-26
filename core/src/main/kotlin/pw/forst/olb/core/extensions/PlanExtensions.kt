package pw.forst.olb.core.extensions

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.dto.jobPlanView
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.evaluation.CompletePlan

fun Plan.asCompletePlan(): CompletePlan? =
    if (cost != null) CompletePlan(
        assignments = assignments.mapNotNull { it.toCompleteAssignment() },
        jobDomain = jobDomain,
        resourcesStackDomain = resourcesStackDomain,
        times = times,
        cost = cost
    )
    else null


fun Plan.toJobPlanViews(): Collection<JobPlanView> {
    val jobs = this.assignments
        .mapNotNull { it.toCompleteAssignment() }
        .groupBy { it.job }
    return this.jobDomain
        .associate { it to (jobs[it] ?: emptyList()) }
        .map { (job, jobData) -> jobPlanView(job, jobData) }
}

fun CompletePlan.toJobPlanViews(): Collection<JobPlanView> {
    val jobs = this.assignments.groupBy { it.job }
    return this.jobDomain
        .associate { it to (jobs[it] ?: emptyList()) }
        .map { (job, jobData) -> jobPlanView(job, jobData) }
}
