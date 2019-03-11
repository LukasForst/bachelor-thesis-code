package pw.forst.olb.common.dto.planning

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.job.JobAssignmentImpl
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.resources.ResourcesProvider
import java.util.SortedMap

data class SimplePlan(
    val assignments: SortedMap<Time, Map<Job, JobAssignmentImpl>>
)

interface PlanView {

    val start: Time

    val end: Time

    val granularity: Time

    /**
     * In the background it should be [SortedMap]
     * */
    val timeViews: Map<Time, TimePlanView>

    /**
     * Cost of all resources used in this plan view
     * */
    val resourcesCost: Cost
}

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

interface InsertablePlanView : PlanView {

    fun insert(time: Time, view: TimePlanView): InsertablePlanView
}

interface InsertableTimePlanView : TimePlanView {

    fun insert(jobAssignment: JobAssignment): InsertableTimePlanView
}
