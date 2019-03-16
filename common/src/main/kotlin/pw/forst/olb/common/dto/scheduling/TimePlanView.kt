package pw.forst.olb.common.dto.scheduling

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.resources.ResourcesProvider

/**
 * SimplePlan view for one time
 * */
interface TimePlanView {

    val jobs: Collection<Job>

    val unAssignedJobs: Collection<Job>

    val jobAssignments: Map<Job, JobAssignment>

    val resourcesProviders: Collection<ResourcesProvider>

    val availableResources: Map<ResourcesProvider, ResourcesPool>

    val resourcesCost: Cost
}
