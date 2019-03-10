package pw.forst.olb.common.dto.planning

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesStack

data class PlanningInput(
    val jobs: Collection<Job>,

    val resourcesStack: Collection<ResourcesStack>,

    val currentPlan: Plan,

    val currentTime: Time,

    val planHorizon: Time,

    val timeStep: Time = Time(60)
)
