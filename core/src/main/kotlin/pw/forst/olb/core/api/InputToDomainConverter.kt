package pw.forst.olb.core.api

import pw.forst.olb.common.dto.AllocationPlanWithHistory
import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.SchedulingInput
import pw.forst.olb.common.dto.SchedulingProperties
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.SimpleResourcesAllocation
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.resources.ResourcesPool
import pw.forst.olb.common.dto.step
import pw.forst.olb.common.dto.until
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.domain.PlanJobAssignment
import java.util.UUID

class InputToDomainConverter {

    fun convert(input: SchedulingInput): Plan {
        val times = (input.startTime until input.endTime step input.timeStep).toList()
        val resourcesAllocation = input.resources.flatMap { it.splitToGranularity() }.sortedBy { it.cost }
        val assignments = generateAssignments(times, resourcesAllocation).sortedBy { it.cost }

        return Plan(
            uuid = UUID.randomUUID(),
            startTime = input.startTime,
            endTime = input.endTime,
            timeIncrement = input.timeStep,
            assignments = assignments,
            jobDomain = input.jobs,
            resourcesStackDomain = resourcesAllocation,
            times = times
        )
    }

    fun convert(plan: AllocationPlanWithHistory, properties: SchedulingProperties): Plan {
        val existingAssignments = plan.timeSchedule.toJobAssignments()
        val times = (properties.startTime until properties.endTime step properties.timeStep).toList()
        val resources = plan.resourcesPools.flatMap { it.splitToGranularity() }

        return Plan(
            startTime = properties.startTime,
            endTime = properties.endTime,
            timeIncrement = properties.timeStep,
            assignments = generateAssignments(existingAssignments, times, resources),
            jobDomain = plan.jobs,
            resourcesStackDomain = resources,
            times = times
        )
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

    private fun generateAssignments(existing: Collection<PlanJobAssignment>, times: Collection<Time>, resources: Collection<ResourcesAllocation>) =
        times.flatMap { time ->
            resources.mapNotNull { resource ->
                if (existing.any { it.time == time && it.allocation == resource }) null
                else
                    PlanJobAssignment(
                        uuid = UUID.randomUUID(),
                        job = null,
                        time = time,
                        allocation = resource
                    )
            }
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

        while (currentPool.cpuResources > smallestCpu && currentPool.memoryResources > smallestMemory) {
            var usedCpu = smallestCpu.copy()
            var usedMemory = smallestMemory.copy()

            currentPool -= usedCpu
            currentPool -= usedMemory

            if (currentPool.cpuResources < smallestCpu || currentPool.memoryResources < smallestMemory) {
                usedCpu += currentPool.cpuResources
                usedMemory += currentPool.memoryResources
            }

            resultCollection.add(
                SimpleResourcesAllocation(
                    provider = currentPool.provider,
                    cpuResources = usedCpu,
                    memoryResources = usedMemory
                )
            )
        }
        return resultCollection
    }


}
