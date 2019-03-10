package pw.forst.olb.simulation

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.job.AlgorithmType
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobPlanningData
import pw.forst.olb.common.dto.job.JobType
import pw.forst.olb.common.dto.planning.Plan
import pw.forst.olb.common.dto.planning.PlanningInput
import pw.forst.olb.common.dto.resources.CpuCost
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryCost
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesStack
import pw.forst.olb.core.Scheduler
import pw.forst.olb.core.naive.NaiveEvaluator
import pw.forst.olb.core.naive.scheduler.CspScheduler

fun main() {
    val scheduler = CspScheduler() as Scheduler
    val evaluator = NaiveEvaluator()

    val result = scheduler.createPlan(createPlanningInput())

    val cost = evaluator.evaluate(result)

    println(result)
    println(cost)
}

fun createPlanningInput() = PlanningInput(
    jobs = obtainJobs(),
    resourcesStack = obtainResourcesStacks(),
    currentPlan = obtainPlan(),
    currentTime = Time(0),
    planHorizon = Time(60 * 10),
    timeStep = Time(60)
)

fun obtainPlan() = Plan(
    assignments = sortedMapOf()
)

fun obtainJobs() = listOf(
    Job(
        parameters = JobParameters(
            maxTime = Time(
                seconds = 60 * 10
            ),
            maxCost = createCost(
                value = Double.MAX_VALUE
            ),
            jobType = JobType.SINGLE_CORE_HEAVY,
            algorithmType = AlgorithmType.TASP
        ),
        data = JobPlanningData(
            json = "w/e"
        ),
        name = "Job #1"
    ),
    Job(
        parameters = JobParameters(
            maxTime = Time(
                seconds = 60 * 1
            ),
            maxCost = createCost(
                value = Double.MAX_VALUE
            ),
            jobType = JobType.SINGLE_CORE_HEAVY,
            algorithmType = AlgorithmType.TASP
        ),
        data = JobPlanningData(
            json = "w/e"
        ),
        name = "Job #2"
    )

)

fun obtainResourcesStacks() = listOf(
    ResourcesStack(
        name = "Amazon Stack",

        memoryResources = MemoryResources(
            memoryInMegaBytes = 1024 * 10
        ),
        memoryCost = MemoryCost(
            cost = createCost(100.0)
        ),

        cpuResources = CpuResources(
            cpusPercentage = 4.0,
            type = CpuPowerType.SINGLE_CORE
        ),
        cpuCost = CpuCost(
            cost = createCost(200.0)
        )
    )
)
