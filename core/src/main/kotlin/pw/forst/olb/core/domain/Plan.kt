package pw.forst.olb.core.domain

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import java.util.UUID

@PlanningSolution
data class Plan(

    override val uuid: UUID = UUID.randomUUID(),

    override val startTime: Time,

    override val endTime: Time,

    override val timeIncrement: Time,

    @field:PlanningEntityCollectionProperty
    val assignments: Collection<PlanJobAssignment>,

    @field:ValueRangeProvider(id = "jobRange")
    @field:ProblemFactCollectionProperty
    val jobDomain: Collection<Job>,

    @field:ProblemFactCollectionProperty
    val resourcesStackDomain: Collection<ResourcesAllocation>,

    @field:ProblemFactCollectionProperty
    val times: Collection<Time>,

    @PlanningScore
    val cost: HardSoftBigDecimalScore = HardSoftBigDecimalScore.ZERO
) : GenericPlan {

    @Suppress("unused") // there has to be empty constructor for optaplanner
    private constructor() : this(
        UUID.randomUUID(),
        TimeImpl.default,
        TimeImpl.default,
        TimeImpl.default,
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        HardSoftBigDecimalScore.ZERO
    )
}

