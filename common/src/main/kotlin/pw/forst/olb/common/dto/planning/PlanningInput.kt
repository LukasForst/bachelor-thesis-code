package pw.forst.olb.common.dto.planning

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesProvider
import pw.forst.olb.common.dto.resources.ResourcesStack

data class PlanningInput(

    val jobs: Collection<Job>,

    val resourcesStack: Collection<ResourcesStack>,

    val currentPlan: SimplePlan,

    val currentTime: Time,

    val planHorizon: Time,

    val timeStep: Time = Time(60)
)

interface SchedulingInputData {

    val planView: PlanView

    val startTime: Time

    val planHorizon: Time

    /**
     * How long should execution run in seconds
     * */
    val schedulingDeadline: Long

    val newJobs: Collection<Job>

    val newResourcesProviders: Collection<ResourcesProvider>
}
