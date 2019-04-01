package pw.forst.olb.core.domain

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import java.util.UUID

@PlanningSolution
data class Plan(

    override val uuid: UUID = UUID.randomUUID(),

    val startTime: Time? = null,

    val endTime: Time? = null,

    val timeIncrement: Time? = null,

    @field:PlanningEntityCollectionProperty
    val assignments: Collection<PlanJobAssignment> = emptyList(),

    @field:ValueRangeProvider(id = "jobRange")
    @field:ProblemFactCollectionProperty
    val jobDomain: Collection<Job> = emptyList(),

    @field:ProblemFactCollectionProperty
    val resourcesStackDomain: Collection<ResourcesAllocation> = emptyList(),

    @field:ProblemFactCollectionProperty
    val times: Collection<Time> = emptyList(),

    @PlanningScore
    val cost: HardSoftBigDecimalScore? = null
) : GenericPlan
