@file:Suppress("SameParameterValue")

package pw.forst.olb.core.evaluator

import org.junit.jupiter.api.Test
import org.optaplanner.core.api.solver.SolverFactory
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.impl.CpuCostImpl
import pw.forst.olb.common.dto.impl.MemoryCostImpl
import pw.forst.olb.common.dto.impl.SimpleClient
import pw.forst.olb.common.dto.impl.SimpleJob
import pw.forst.olb.common.dto.impl.SimpleJobParameters
import pw.forst.olb.common.dto.impl.SimpleResourcesAllocation
import pw.forst.olb.common.dto.impl.SimpleResourcesProvider
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobType
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesProvider
import pw.forst.olb.common.dto.sum
import pw.forst.olb.common.extensions.duration
import pw.forst.olb.common.extensions.minMaxBy
import pw.forst.olb.common.extensions.minMaxValueBy
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.domain.PlanJobAssignment
import pw.forst.olb.core.evaluation.LoggingPlanEvaluator
import pw.forst.olb.core.extensions.asCompletePlan
import pw.forst.olb.core.extensions.toJobPlanViews
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class SolverTest {
    @Test
    fun run() {
        val solverFactory = SolverFactory.createFromXmlResource<Plan>(
            "solverConfiguration.xml"
        )
        val solver = solverFactory.buildSolver()

        val plan = generatePlan(jobsCount = 5, resourcesStacksCount = 200, timeCount = 100L)
        val result = solver.solve(plan)
        val finalPlan = result.asCompletePlan()

        println("Plan computed!\n")

        val finalScore = LoggingPlanEvaluator().calculateScore(result)

        println("\n\n******\nFinal score:")

        println(finalScore)


        val toPrint = finalPlan
            .toJobPlanViews()
            .joinToString("\n") { formateJobPlanView(it) }

        println("\n$toPrint")

    }

    private fun formateJobPlanView(view: JobPlanView): String =
        "Job: ${view.job.name}\n" +
                "Time spent: ${view.assignments.minMaxValueBy { it.time }?.duration()?.position}, Max Time: ${view.job.parameters.maxTime.position}\n" +
                "Cost paid: ${view.assignments.map { it.cost }.sum().value}, Max Cost: ${view.job.parameters.maxCost.value}\n" +
                "No. of assignments: ${view.assignments.size}\n"

    private fun generatePlan(
        jobsCount: Int,
        resourcesStacksCount: Int,
        timeCount: Long
    ): Plan {
        val resources = generateResourcesDomain(resourcesStacksCount)
        val times = generateTime(timeCount)
        val jobs = generateJobs(jobsCount, times.max()!!.position - times.min()!!.position)
        val assignments = generateAssignments(times, resources)

        val (minTime, maxTime) = times.minMaxBy { it.position }!!

        return Plan(
            uuid = UUID.randomUUID(),
            timeIncrement = TimeImpl(1, TimeUnit.SECONDS),
            startTime = minTime,
            endTime = maxTime,
            assignments = assignments,
            jobDomain = jobs,
            resourcesStackDomain = resources,
            times = times
        )
    }

    private fun generateTime(count: Long): List<Time> = (0L until count).map { TimeImpl(it, TimeUnit.SECONDS) }

    private fun generateResourcesDomain(count: Int): List<ResourcesAllocation> = (0 until count).map {
        SimpleResourcesAllocation(
            provider = randomResourcesProvider(it),
            cpuResources = smallestCpuResources(),
            memoryResources = smallestMemoryResources()
        )
    }

    private fun smallestMemoryResources(): MemoryResources = MemoryResources(memoryInMegaBytes = 512L)

    private fun smallestCpuResources(): CpuResources = CpuResources(cpuValue = 1.0, type = CpuPowerType.MULTI_CORE)

    private fun randomMemoryResources(seed: Int): MemoryResources = MemoryResources(memoryInMegaBytes = (1024 * Random(seed).nextInt(1, 20)).toLong())

    private fun randomCpuResources(seed: Int): CpuResources = CpuResources(cpuValue = 4.0 * Random(seed).nextDouble(1.0, 10.0), type = CpuPowerType.MULTI_CORE)

    private val resourcesProviders: Collection<ResourcesProvider> = listOf(
        SimpleResourcesProvider(
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(10.0),
            memoryCost = MemoryCostImpl(1.0),
            name = "Expensive resources provider"
        ),
        SimpleResourcesProvider(
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(1.0),
            memoryCost = MemoryCostImpl(0.01),
            name = "Cheap resources provider"
        )
    )

    private fun randomResourcesProvider(seed: Int): ResourcesProvider = resourcesProviders.random(Random(seed))

    private fun generateJobs(count: Int, totalTimeRunning: Long): List<Job> =
        (0 until count).map {
            SimpleJob(
                parameters = randomParameters(it, totalTimeRunning),
                client = randomClient(it),
                uuid = UUID.randomUUID(),
                name = it.toString()
            )
        }


    private fun randomParameters(seed: Int, totalTimeRunning: Long): JobParameters {
        val rd = Random(seed)
        val maxRunningTime = (totalTimeRunning * rd.nextDouble(0.2, 0.7)).toLong()
        return SimpleJobParameters(
            maxTime = TimeImpl(position = maxRunningTime, units = TimeUnit.SECONDS),
            maxCost = createCost(rd.nextInt(50, 150).toDouble()),
            jobType = JobType.PARALELIZED
        )
    }

    private fun randomClient(seed: Int): Client = SimpleClient(name = "Random client $seed", uuid = UUID.randomUUID())


    private fun generateAssignments(times: Collection<Time>, resources: Collection<ResourcesAllocation>) =
        times.flatMap { time ->
            resources.map { resource ->
                PlanJobAssignment(
                    uuid = UUID.randomUUID(),
                    job = null,
                    time = time,
                    allocation = resource
                )
            }

        }
}
