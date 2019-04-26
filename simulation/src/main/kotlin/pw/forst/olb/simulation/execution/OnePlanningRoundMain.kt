package pw.forst.olb.simulation.execution

import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.impl.ClientImpl
import pw.forst.olb.common.dto.impl.CpuCostImpl
import pw.forst.olb.common.dto.impl.InputResourcesPool
import pw.forst.olb.common.dto.impl.JobParametersImpl
import pw.forst.olb.common.dto.impl.MemoryCostImpl
import pw.forst.olb.common.dto.impl.SchedulingInputImpl
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobType
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.extensions.prettyFormat
import pw.forst.olb.core.api.InputToDomainConverter
import pw.forst.olb.core.api.OlbCoreApiImpl
import pw.forst.olb.core.domain.PlanningJob
import pw.forst.olb.core.solver.OptaplannerSolverFactory
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random

open class OnePlanningRoundMain {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val schedulingFunction = OnePlanningRoundMain().buildScheduler()
            println("\n\n${schedulingFunction().prettyFormat()}\n")
        }

    }

    fun buildScheduler(): () -> AllocationPlan {
        val api = buildApi()
        val input = createSchedulingInput(
            3,
            50,
            TimeImpl(20, TimeUnit.SECONDS),
            4
        )

        return { api.createNewPlan(input) }
    }


    @Suppress("SameParameterValue") //integration test
    private fun createSchedulingInput(jobsCount: Int, planningHorizon: Long, runningTime: Time, cores: Int? = null): SchedulingInput {
        val (start, end, step) = generateTimes(planningHorizon)
        return SchedulingInputImpl(
            resources = resourcePools(),
            jobs = generateJobs(jobsCount, planningHorizon),
            startTime = start,
            endTime = end,
            timeStep = step,
            maxTimePlanningSpend = runningTime,
            cores = cores
        )
    }

    private fun generateTimes(duration: Long): Triple<Time, Time, Time> =
        Triple(
            TimeImpl(0, TimeUnit.SECONDS),
            TimeImpl(duration, TimeUnit.SECONDS),
            TimeImpl(1, TimeUnit.SECONDS)
        )

    private fun resourcePools(): Collection<ResourcesPool> = listOf(
        InputResourcesPool(
            name = "Cost: 1 + 0.02",
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(1.0),
            memoryCost = MemoryCostImpl(0.02),
            cpuResources = CpuResources(4.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 32)
        ),
        InputResourcesPool(
            name = "Cost: 1.5 + 0.02",
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(1.5),
            memoryCost = MemoryCostImpl(0.02),
            cpuResources = CpuResources(2.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 64)
        ),
        InputResourcesPool(
            name = "Cost: 10.0 + 0.05",
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(10.0),
            memoryCost = MemoryCostImpl(0.05),
            cpuResources = CpuResources(1.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 64)
        )
    )

    private fun generateJobs(count: Int, totalTimeRunning: Long): List<Job> =
        (0 until count).map {
            PlanningJob(
                parameters = randomParameters(it, totalTimeRunning),
                client = randomClient(it),
                uuid = UUID.randomUUID(),
                name = it.toString()
            )
        }


    private fun randomParameters(seed: Int, totalTimeRunning: Long): JobParameters {
        val rd = Random(seed)
        val maxRunningTime = (totalTimeRunning * rd.nextDouble(0.2, 0.7)).toLong()
        return JobParametersImpl(
            maxTime = TimeImpl(position = maxRunningTime, units = TimeUnit.SECONDS),
            maxCost = createCost(rd.nextInt(50, 250).toDouble()),
            jobType = JobType.PARALELIZED
        )
    }

    private fun randomClient(seed: Int): Client = ClientImpl(name = "Random client $seed", uuid = UUID.randomUUID())

    protected open fun buildApi(): OlbCoreApi = OlbCoreApiImpl(InputToDomainConverter(), OptaplannerSolverFactory(), true)
}
