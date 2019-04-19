package pw.forst.olb.simulation.execution

import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.impl.ClientImpl
import pw.forst.olb.common.dto.impl.CpuCostImpl
import pw.forst.olb.common.dto.impl.InputResourcesPool
import pw.forst.olb.common.dto.impl.JobParametersImpl
import pw.forst.olb.common.dto.impl.MemoryCostImpl
import pw.forst.olb.common.dto.impl.SchedulingPropertiesImpl
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobType
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.core.api.InputToDomainConverter
import pw.forst.olb.core.api.OlbCoreApiImpl
import pw.forst.olb.core.domain.PlanningJob
import pw.forst.olb.core.solver.OptaplannerSolverFactory
import pw.forst.olb.simulation.input.data.DataParser
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ExecutionConfiguration {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val configuration = ExecutionConfiguration()
            val executor = configuration.obtainPeriodicExecutor()
            executor.startTest("./job-data/input", 10, configuration.generateJobs(10, TimeUnit.MINUTES.toSeconds(10)), configuration.resourcePools())
        }
    }

    fun obtainPeriodicExecutor(): PeriodicExecutor =
        PeriodicExecutor(
            dataParser = DataParser(),
            coreApi = OlbCoreApiImpl(InputToDomainConverter(), OptaplannerSolverFactory(), true),
            schedulingProperties = generateSchedulingProperties()
        )


    private fun generateSchedulingProperties(): SchedulingProperties = SchedulingPropertiesImpl(
        startTime = TimeImpl(0, TimeUnit.SECONDS),
        endTime = TimeImpl(10, TimeUnit.MINUTES),
        timeStep = TimeImpl(1, TimeUnit.MINUTES),
        maxTimePlanningSpend = TimeImpl(20, TimeUnit.SECONDS),
        cores = 1
    )

    fun resourcePools(): Collection<ResourcesPool> = listOf(
        InputResourcesPool(
            name = "Cost: 1 + 0.02",
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(1.0),
            memoryCost = MemoryCostImpl(0.02),
            cpuResources = CpuResources(5.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 64)
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
            cpuResources = CpuResources(2.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 64)
        )
    )

    fun generateJobs(count: Int, totalTimeRunning: Long): List<Job> =
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
            maxCost = createCost(rd.nextInt(250, 1000).toDouble()),
            jobType = JobType.PARALELIZED
        )
    }

    private fun randomClient(seed: Int): Client = ClientImpl(name = "Random client $seed", uuid = UUID.randomUUID())

}
