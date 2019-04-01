package pw.forst.olb.core.evaluation

import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import java.util.UUID


data class CompletePlan(

    override val uuid: UUID,

    val startTime: Time,

    val endTime: Time,

    val timeIncrement: Time,

    val assignments: Collection<CompleteJobAssignment>,

    val jobDomain: Collection<Job>,

    val resourcesStackDomain: Collection<ResourcesAllocation>,

    val times: Collection<Time>,

    val cost: HardSoftBigDecimalScore
) : GenericPlan
