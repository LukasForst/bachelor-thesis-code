package pw.forst.olb.core.api

import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.JobImpl
import pw.forst.olb.common.dto.impl.JobParametersImpl
import pw.forst.olb.common.dto.impl.ResourcesAllocationImpl
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.sum
import pw.forst.olb.common.dto.until
import pw.forst.olb.common.dto.withStep
import pw.forst.olb.common.extensions.assert
import pw.forst.olb.common.extensions.mapToSet
import pw.forst.olb.common.extensions.minValueBy
import pw.forst.olb.common.extensions.sumByLong
import pw.forst.olb.common.extensions.swapKeys
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.domain.PlanJobAssignment
import pw.forst.olb.core.extensions.asSchedulingEntity
import java.util.UUID

class InputToDomainConverter {

    fun convert(input: SchedulingInput): Plan {
        val times = (input.startTime until input.endTime withStep input.timeStep).toList()
        val resourcesAllocation = input.resources.flatMap { it.splitToGranularity() }.sortedBy { it.cost }
        val assignments = generateAssignments(times, resourcesAllocation).sortedBy { it.cost }

        return Plan(
            uuid = UUID.randomUUID(),
            startTime = input.startTime,
            endTime = input.endTime,
            timeIncrement = input.timeStep,
            assignments = assignments,
            jobDomain = input.jobs.map { it.asSchedulingEntity() },
            resourcesStackDomain = resourcesAllocation,
            times = times
        )
    }

    fun convert(plan: AllocationPlanWithHistory, properties: SchedulingProperties): Plan {
        val preStartTime = properties.startTime - properties.timeStep
        val existingRelevantAssignments = plan.timeSchedule.filterKeys { it >= preStartTime }.toJobAssignments()

        val times = (preStartTime until properties.endTime withStep properties.timeStep).toList()
        val resources = plan.resourcesPools.flatMap { it.splitToGranularity() }

        return Plan(
            startTime = properties.startTime,
            endTime = properties.endTime,
            timeIncrement = properties.timeStep,
            assignments = generateAssignments(existingRelevantAssignments, times, resources),
            jobDomain = reduceJobs(plan.timeSchedule.filterKeys { it < preStartTime }, plan.jobs, properties).map { it.asSchedulingEntity() },
            resourcesStackDomain = resources,
            times = times
        )
    }

    private fun reduceJobs(nonRelevantAssignments: Map<Time, Collection<JobResourcesAllocation>>, jobs: Collection<Job>, properties: SchedulingProperties): Collection<Job> {
        val jobsData = nonRelevantAssignments.mapValues { (_, all) -> all.groupBy { it.job } }
            .swapKeys()
            .mapValues { (job, timeData) ->
                Pair(
                    job.parameters.maxTime - (properties.startTime - timeData.keys.minValueBy { it }!!),
                    job.parameters.maxCost - timeData.map { (_, a) -> a.map { it.allocation.cost }.sum() }.sum()
                )
            }

        return jobs.map {
            val newData = jobsData[it]
            if (newData != null) {
                JobImpl(
                    parameters = JobParametersImpl(
                        maxTime = newData.first,
                        maxCost = newData.second,
                        jobType = it.parameters.jobType
                    ),
                    client = it.client,
                    uuid = it.uuid,
                    name = it.name
                )
            } else it
        }
    }

    private fun Map<Time, Collection<JobResourcesAllocation>>.toJobAssignments(): Collection<PlanJobAssignment> =
        this.flatMap { (time, all) ->
            all.flatMap { jobResourcesAllocation ->
                jobResourcesAllocation.allocation.splitToGranularity().map {
                    PlanJobAssignment(
                        uuid = UUID.randomUUID(),
                        job = jobResourcesAllocation.job,
                        time = time,
                        allocation = it
                    )
                }
            }
        }

    private fun generateAssignments(existing: Collection<PlanJobAssignment>, times: Collection<Time>, resourcePools: Collection<ResourcesPool>): Collection<PlanJobAssignment> {
        return times.flatMap { time ->
            val relevantExistingAssignments = existing.filter { it.time == time }.groupBy { it.allocation?.provider }
            relevantExistingAssignments.values.flatten() + resourcePools.flatMap { pool ->
                val (usedCpu, usedMemory) = relevantExistingAssignments[pool.provider]?.mapNotNull { it.allocation }.getCpuAndMemorySum()
                val reducedPool = pool - usedCpu - usedMemory
                reducedPool.splitToGranularity().map {
                    PlanJobAssignment(
                        uuid = UUID.randomUUID(),
                        job = null,
                        time = time,
                        allocation = it
                    )
                }
            }
        }
    }


    private fun Collection<ResourcesAllocation>?.getCpuAndMemorySum(): Pair<CpuResources, MemoryResources> {
        if (this == null || this.isEmpty()) return CpuResources(0.0, CpuPowerType.MULTI_CORE) to MemoryResources(0L)

        val cpu = this.map { it.cpuResources }.let { cpu ->
            assert { cpu.mapToSet { it.type }.size == 1 }
            CpuResources(cpu.sumByDouble { it.cpuValue }, cpu.first().type)
        }
        val memory = MemoryResources(this.map { it.memoryResources }.sumByLong { it.memoryInMegaBytes })
        return cpu to memory
    }

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

    private fun ResourcesPool.splitToGranularity(): Collection<ResourcesAllocation> {
        val smallestCpu = CpuResources.getSmallest(this.cpuResources.type)
        val smallestMemory = MemoryResources.getSmallest()

        val resultCollection = mutableListOf<ResourcesAllocation>()
        var currentPool = this

        while (currentPool.cpuResources >= smallestCpu && currentPool.memoryResources >= smallestMemory) {
            var usedCpu = smallestCpu.copy()
            var usedMemory = smallestMemory.copy()

            currentPool -= usedCpu
            currentPool -= usedMemory

            if (currentPool.cpuResources < smallestCpu || currentPool.memoryResources < smallestMemory) {
                usedCpu += currentPool.cpuResources
                usedMemory += currentPool.memoryResources
            }

            resultCollection.add(
                ResourcesAllocationImpl(
                    provider = currentPool.provider,
                    cpuResources = usedCpu,
                    memoryResources = usedMemory
                )
            )
        }
        return resultCollection
    }


}
