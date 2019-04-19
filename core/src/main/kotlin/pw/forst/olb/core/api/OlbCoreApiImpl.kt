package pw.forst.olb.core.api

import mu.KLogging
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.SolverConfiguration
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.AllocationPlanImpl
import pw.forst.olb.common.dto.impl.IterationImpl
import pw.forst.olb.common.dto.impl.JobResourceAllocationImpl
import pw.forst.olb.common.dto.impl.ResourcesPoolImpl
import pw.forst.olb.common.dto.impl.createEmptyResourcesAllocation
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.sum
import pw.forst.olb.common.dto.sumCosts
import pw.forst.olb.common.extensions.assert
import pw.forst.olb.common.extensions.mapToSet
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.evaluation.BigDecimalScoreCalculation
import pw.forst.olb.core.evaluation.CompletePlan
import pw.forst.olb.core.evaluation.LoggingPlanEvaluator
import pw.forst.olb.core.extensions.asCompletePlan
import pw.forst.olb.core.extensions.asSchedulingEntity
import pw.forst.olb.core.predict.JobPrediction
import pw.forst.olb.core.predict.factory.PredictionStoreFactory
import pw.forst.olb.core.solver.OptaplannerSolverFactory

class OlbCoreApiImpl(
    private val inputToDomainConverter: InputToDomainConverter,
    private val solverFactory: OptaplannerSolverFactory,
    private val finalLogEnabled: Boolean = false
) : OlbCoreApi {

    private companion object : KLogging() {
        const val DEFAULT_THREADS = 1
    }

    override fun createNewPlan(input: SchedulingInput): AllocationPlan {
        val domain = inputToDomainConverter.convert(input)
        return solveDomain(domain, input.toSolverConfiguration()).toAllocationPlan()
    }


    override fun enhancePlan(plan: AllocationPlanWithHistory, properties: SchedulingProperties): AllocationPlan {
        val prediction = JobPrediction()
        val cachedPrediction = plan.jobsData.mapNotNull { prediction.createPrediction(it, getLastIterationPrediction(plan, it)) }
        PredictionStoreFactory.addToStore(cachedPrediction)
        val domain = inputToDomainConverter.convert(plan, properties)

        val alreadyPlannedAssignments = plan.timeSchedule.filterKeys { it < properties.startTime }

        return solveDomain(domain, properties.toSolverConfiguration())
            .toAllocationPlan(plan.jobs, alreadyPlannedAssignments).also {
                assert { it.resourcesPools.size == plan.resourcesPools.size }
            }
    }

    private fun solveDomain(domain: Plan, configuration: SolverConfiguration): CompletePlan {
        val solver = solverFactory.create<Plan>(configuration)

        logger.info { "Starting solving plan: ${BigDecimalScoreCalculation().calculateScore(domain)}" }

        val solution = solver.solve(domain)
        if (finalLogEnabled) LoggingPlanEvaluator().calculateScore(solution)

        return solution.asCompletePlan()
    }

    private fun getLastIterationPrediction(@Suppress("UNUSED_PARAMETER") plan: AllocationPlanWithHistory, job: JobWithHistory): Iteration {
        //TODO get correct prediction
        return (job.jobValueDuringIterations.keys.max() ?: IterationImpl(0)) + IterationImpl(2000)

    }

    private fun CompletePlan.toAllocationPlan(allJobs: Collection<Job>, filteredTimeSchedule: Map<Time, Collection<JobResourcesAllocation>>): AllocationPlan {
        val assignments = this.assignments.toTimeSchedule(filteredTimeSchedule)
        return AllocationPlanImpl(
            uuid = this.uuid,
            startTime = this.startTime,
            endTime = this.endTime,
            timeIncrement = this.timeIncrement,
            timeSchedule = assignments,
            jobs = allJobs.map { it.asSchedulingEntity() },
            resourcesPools = this.resourcesStackDomain.toResourcesPools(),
            cost = assignments.computeCost()
        )
    }

    private fun Map<Time, Collection<JobResourcesAllocation>>.computeCost(): Cost = this.flatMap { (_, a) -> a.map { it.allocation.cost } }.sum()


    private fun CompletePlan.toAllocationPlan(): AllocationPlan =
        AllocationPlanImpl(
            uuid = this.uuid,
            startTime = this.startTime,
            endTime = this.endTime,
            timeIncrement = this.timeIncrement,
            timeSchedule = this.assignments.toTimeSchedule(),
            jobs = this.jobDomain.map { it.asSchedulingEntity() },
            resourcesPools = this.resourcesStackDomain.toResourcesPools(),
            cost = this.assignments.map { it.cost }.sumCosts()
        )

    private fun Collection<CompleteJobAssignment>.toTimeSchedule(dataToAdd: Map<Time, Collection<JobResourcesAllocation>>): Map<Time, Collection<JobResourcesAllocation>> {
        val newTimeSchedule = this.toTimeSchedule()
        val allKeys = newTimeSchedule.keys + dataToAdd.keys
        return allKeys.associate { time ->
            time to (newTimeSchedule[time] ?: emptyList()) + (dataToAdd[time] ?: emptyList())
        }
    }

    private fun Collection<CompleteJobAssignment>.toTimeSchedule(): Map<Time, Collection<JobResourcesAllocation>> =
        this.groupBy { it.time }
            .mapValues { (_, asg) ->
                asg.groupBy({ it.job }, { it.allocation })
                    .mapValues { (_, allocations) -> allocations.merge() }
                    .toSingleObject()
            }

    private fun Map<Job, ResourcesAllocation>.toSingleObject(): Collection<JobResourcesAllocation> =
        this.map { (job, allocation) -> JobResourceAllocationImpl(job, allocation) }

    private fun Collection<ResourcesAllocation>.merge(): ResourcesAllocation {
        assert { !this.isEmpty() }
        assert { this.mapToSet { it.provider }.size == 1 }

        val first = this.first()
        val init = createEmptyResourcesAllocation(first.provider, first.cpuResources.type)
        return this.fold(init) { acc, x -> acc + x }
    }

    private data class AllocationSum(val cpu: CpuResources, val mem: MemoryResources)

    private fun Collection<ResourcesAllocation>.toResourcesPools(): Collection<ResourcesPool> =
        this.groupBy { it.provider }
            .mapValues { (_, allocations) ->
                val init = AllocationSum(CpuResources.getZero(allocations.first().cpuResources.type), MemoryResources.ZERO)
                allocations.fold(init) { acc, allocation -> acc.copy(cpu = acc.cpu + allocation.cpuResources, mem = acc.mem + allocation.memoryResources) }

            }
            .map { (provider, sum) -> ResourcesPoolImpl(provider = provider, cpuResources = sum.cpu, memoryResources = sum.mem) }


    private fun SchedulingProperties.toSolverConfiguration() =
        SolverConfiguration(
            maxTimeInSeconds = maxTimePlanningSpend.units.toSeconds(maxTimePlanningSpend.position),
            threads = cores ?: DEFAULT_THREADS
        )

}
