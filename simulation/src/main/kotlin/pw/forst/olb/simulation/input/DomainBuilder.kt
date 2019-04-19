package pw.forst.olb.simulation.input

import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.createCost
import pw.forst.olb.common.dto.impl.AllocationPlanWithHistoryImpl
import pw.forst.olb.common.dto.impl.ClientImpl
import pw.forst.olb.common.dto.impl.CpuCostImpl
import pw.forst.olb.common.dto.impl.GenericPlanImpl
import pw.forst.olb.common.dto.impl.InputResourcesPool
import pw.forst.olb.common.dto.impl.JobParametersImpl
import pw.forst.olb.common.dto.impl.JobResourceAllocationImpl
import pw.forst.olb.common.dto.impl.MemoryCostImpl
import pw.forst.olb.common.dto.impl.ResourcesAllocationImpl
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobType
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.until
import pw.forst.olb.common.dto.withStep
import pw.forst.olb.simulation.input.data.DataParser
import pw.forst.olb.simulation.input.data.JobWithHistoryFactory
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DomainBuilder(
    private val dataParser: DataParser,
    private val jobWithHistoryFactory: JobWithHistoryFactory
) {

    fun build(folderPath: String): AllocationPlanWithHistory {
        val runtimeInfo = dataParser.readFolder(folderPath)
        val maxTimePointInMs = runtimeInfo.flatMap { cl -> cl.data.map { it.timePoint } }.max()!!

        val plan = generatePlan(maxTimePointInMs)
        val totalTime = (plan.endTime - plan.startTime).position

        val jobs = runtimeInfo.mapIndexed { idx, x ->
            jobWithHistoryFactory.create(x, plan, randomParameters(idx, totalTime), randomClient(idx))
        }

        val (_, timeSchedule) = generateTimeSchedule(plan, jobs)
        return AllocationPlanWithHistoryImpl(
            jobsData = jobs,
            timeSchedule = timeSchedule,
            resourcesPools = resourcePools(),
            genericPlan = plan
        )
    }

    private fun generateTimeSchedule(genericPlan: GenericPlan, jobs: Collection<JobWithHistory>): Pair<InputResourcesPool, Map<Time, Collection<JobResourcesAllocation>>> {
        val correctData = (genericPlan.startTime until genericPlan.endTime withStep genericPlan.timeIncrement).associate { time ->
            time to jobs.filter { job -> job.iterationsInTimes[time]?.isNotEmpty() ?: false }
        }
        val maxAllocationInTime = correctData.map { it.value.size }.max()!!
        val initPool = createInitPool(CpuResources(maxAllocationInTime.toDouble(), CpuPowerType.SINGLE_CORE), MemoryResources.getSmallest() * maxAllocationInTime)
        return initPool to correctData.mapValues { (_, l) -> l.map { JobResourceAllocationImpl(it, defaultAllocation(initPool)) } }
    }


    private fun generatePlan(maxTimePointInMs: Time): GenericPlan =
        GenericPlanImpl(
            uuid = UUID.randomUUID(),
            startTime = TimeImpl(0, TimeUnit.SECONDS),
            endTime = maxTimePointInMs,
            timeIncrement = TimeImpl(1, TimeUnit.SECONDS)
        )

    private fun defaultAllocation(initPool: InputResourcesPool): ResourcesAllocation = ResourcesAllocationImpl(
        initPool,
        CpuResources.getSmallest(initPool.cpuResources.type),
        MemoryResources.getSmallest()
    )

    private fun createInitPool(cpuResources: CpuResources, mem: MemoryResources = MemoryResources(0)) = InputResourcesPool(
        name = "Init pool",
        uuid = UUID.randomUUID(),
        cpuCost = CpuCostImpl(0.00001),
        memoryCost = MemoryCostImpl(0.00001),
        cpuResources = cpuResources,
        memoryResources = mem
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
            cpuResources = CpuResources(8.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 64)
        ),
        InputResourcesPool(
            name = "Cost: 10.0 + 0.05",
            uuid = UUID.randomUUID(),
            cpuCost = CpuCostImpl(10.0),
            memoryCost = MemoryCostImpl(0.05),
            cpuResources = CpuResources(8.0, CpuPowerType.MULTI_CORE),
            memoryResources = MemoryResources(1024 * 64)
        )
    )

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
}
