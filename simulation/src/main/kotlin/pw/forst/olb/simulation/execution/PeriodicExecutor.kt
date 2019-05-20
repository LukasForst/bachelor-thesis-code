package pw.forst.olb.simulation.execution

import mu.KLogging
import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.impl.AllocationPlanWithHistoryImpl
import pw.forst.olb.common.dto.impl.JobWithHistoryImpl
import pw.forst.olb.common.dto.impl.LengthAwareIterationImpl
import pw.forst.olb.common.dto.impl.SchedulingInputImpl
import pw.forst.olb.common.dto.impl.SchedulingPropertiesImpl
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.extensions.averageByInt
import pw.forst.olb.common.extensions.newLine
import pw.forst.olb.common.extensions.prettyFormat
import pw.forst.olb.simulation.extensions.createTimesMap
import pw.forst.olb.simulation.extensions.toLengthAwareIterationData
import pw.forst.olb.simulation.input.data.AlgorithmRuntimeInfo
import pw.forst.olb.simulation.input.data.DataParser
import java.io.File
import java.util.concurrent.TimeUnit

class PeriodicExecutor(
    private val dataParser: DataParser,
    private val coreApi: OlbCoreApi,
    private val schedulingProperties: SchedulingProperties
) {

    private companion object : KLogging()

    fun startTest(folder: String, numberOfIterations: Int, jobsDomain: Collection<Job>, resourcesPool: Collection<ResourcesPool>): AllocationPlan {
        logger.info { "Reading random job data from $folder" }
        logger.info { "Currently in folder ${File("./").list()}" }
        val schedulingData = dataParser.readFolder(folder)
            .shuffled()
            .zip(jobsDomain) { data, job -> job to data }
            .toMap()
            .also { recordDataMapping(it) }

        logger.info { "Running initial scheduling" }
        val initialPlan = firstRun(jobsDomain, resourcesPool).also {
            logger.info { "Printing Initial plan" }
            println("0 iteration:$newLine${it.prettyFormat(true)}$newLine")
        }

        logger.info { "Running iterative scheduling" }
        return runIteratively(numberOfIterations, initialPlan, schedulingData)
    }

    private fun runIteratively(numberOfIterations: Int, initialPlan: AllocationPlan, schedulingData: Map<Job, AlgorithmRuntimeInfo>): AllocationPlan =
        (0 until numberOfIterations).fold(initialPlan) { plan, idx ->
            logger.info { "Creating data for $idx iteration!" }
            val (allocationPlan, schedulingProperties) = createNextIteration(plan, schedulingData)
            logger.info { "Executing scheduling for $idx iteration!" }
            coreApi.enhancePlan(allocationPlan, schedulingProperties).also {
                logger.info { "Printing plan for $idx iteration" }
                println("$idx iteration:$newLine${it.prettyFormat(true)}$newLine")
            }
        }

    private fun createNextIteration(plan: AllocationPlan, schedulingData: Map<Job, AlgorithmRuntimeInfo>): Pair<AllocationPlanWithHistory, SchedulingProperties> {
        val newStart = plan.startTime + plan.timeIncrement

        val jobsWithHistory = plan.jobs.map { job ->
            val filteredRuntimeInfo = schedulingData.getValue(job).let { info -> info.copy(data = info.data.filter { it.timePoint < newStart }) }
            val iterationData = filteredRuntimeInfo.toLengthAwareIterationData()

            JobWithHistoryImpl(
                plan = plan,
                schedulingStartedTime = TimeImpl(0, TimeUnit.SECONDS),
                averageIteration = LengthAwareIterationImpl(0, iterationData.keys.averageByInt { it.iterationLengthInMls }.toInt()),
                jobValueDuringIterations = iterationData.mapKeys { it.key },
                iterationLengthInTimes = iterationData.keys.createTimesMap(plan.startTime, plan.timeIncrement),
                job = job,
                allocationHistory = plan.timeSchedule
                    .filterKeys { it < newStart }
                    .mapValues { (_, jobs) -> jobs.singleOrNull { it.job == job }?.allocation }
                    .filterValues { it != null }
                    .mapValues { it.value!! }
            )
        }
        val schedulingProperties = SchedulingPropertiesImpl(
            startTime = newStart,
            endTime = schedulingProperties.endTime,
            timeStep = schedulingProperties.timeStep,
            maxTimePlanningSpend = schedulingProperties.maxTimePlanningSpend,
            cores = schedulingProperties.cores
        )
        val allocationPlanWithHistory = AllocationPlanWithHistoryImpl(
            jobsData = jobsWithHistory,
            allocationPlant = plan
        )
        return allocationPlanWithHistory to schedulingProperties
    }

    private fun recordDataMapping(data: Map<Job, AlgorithmRuntimeInfo>) {
        data.forEach { (job, runtimeInfo) -> logger.info { "${job.name} ---- ${runtimeInfo.name}" } }
    }

    private fun firstRun(jobsDomain: Collection<Job>, resourcesPool: Collection<ResourcesPool>): AllocationPlan =
        coreApi.createNewPlan(
            SchedulingInputImpl(
                resources = resourcesPool,
                jobs = jobsDomain,
                schedulingProperties = schedulingProperties
            )
        )

}
