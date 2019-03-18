package pw.forst.olb.common.dto.scheduling

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.resources.ResourcesProvider


interface SchedulingInputData {

    val planView: PlanView

    val startTime: Time

    val planHorizon: Time

    /**
     * How long should execution run in seconds
     * */
    val schedulingDeadline: Long

    val newJobs: Collection<Job>

    val jobValues: Map<Job, JobValue>

    val newResourcesProviders: Collection<ResourcesProvider>
}
