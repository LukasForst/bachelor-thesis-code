package pw.forst.olb.core.naive.scheduler

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.job.JobType
import pw.forst.olb.common.dto.planning.Plan
import pw.forst.olb.common.dto.planning.PlanningInput
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.resources.ResourcesStack
import pw.forst.olb.common.dto.resources.isFor
import pw.forst.olb.common.dto.step
import pw.forst.olb.common.dto.sumOnlyValues
import pw.forst.olb.common.dto.until
import pw.forst.olb.common.extensions.swapKeys
import pw.forst.olb.core.Scheduler
import kotlin.random.Random

class CspScheduler : Scheduler {

    override fun createPlan(input: PlanningInput): Plan {
        val timeStep = input.timeStep

        val variables = createVariables(input)

        val jobAssignments = sortedMapOf<Time, Map<Job, JobAssignment>>()
            .also { it.putAll(input.currentPlan.assignments) }

        var resourcesStacks = input.resourcesStack

        for (time in input.currentTime until input.planHorizon step input.timeStep) {
            if (!jobAssignments.containsKey(time)) jobAssignments[time] = mutableMapOf()

            val jobs = variables.sortedBy { it.jobPriority }

            val timeAssignments = mutableMapOf<Job, JobAssignment>()
            for (job in jobs) {
                val domain = getDomainForJob(job, time, timeStep, jobAssignments, resourcesStacks)
                val stack = preferredStackForJob(job, domain) ?: continue
                val allocation = obtainPreferredAllocation(job, jobAssignments[time - timeStep]?.get(job), stack)
                val assignment = createJobAssignment(job, allocation, time, stack)

                resourcesStacks = resourcesStacks.filter { it isFor stack } + stack.update(assignment)
                timeAssignments[job] = assignment
            }
            jobAssignments[time] = timeAssignments
        }

        return Plan(assignments = jobAssignments)
    }

    private fun createJobAssignment(job: Job, allocation: ResourcesAllocation, time: Time, stack: ResourcesStack): JobAssignment =
        JobAssignment(
            job = job,
            time = time,
            cpu = allocation.cpu,
            memory = allocation.memory,
            resourcesStack = stack
        )

    private fun ResourcesStack.update(jobAssignment: JobAssignment): ResourcesStack = this - jobAssignment.cpu - jobAssignment.memory

    private fun obtainPreferredAllocation(job: Job, previousAllocation: JobAssignment?, stack: ResourcesStack): ResourcesAllocation {
        return randomAllocation(stack, Random.Default)
    }

    private fun randomAllocation(stack: ResourcesStack, rd: Random): ResourcesAllocation = ResourcesAllocation(
        cpu = stack.cpuResources.copy(cpusPercentage = stack.cpuResources.cpusPercentage / rd.nextInt(1, 4)),
        memory = stack.memoryResources.copy(memoryInMegaBytes = stack.memoryResources.memoryInMegaBytes / rd.nextInt(1, 4))
    )

    private fun preferredStackForJob(job: Job, stacks: Collection<ResourcesStack>): ResourcesStack? {
        var seq = stacks.asSequence()
        seq = when (job.parameters.jobType) {
            JobType.PARALELIZED -> seq.filter { it.cpuResources.type == CpuPowerType.MULTI_CORE }
            JobType.SINGLE_CORE_HEAVY -> seq.filter { it.cpuResources.type == CpuPowerType.SINGLE_CORE }
        }
        //TODO more informative decisions
        return seq.firstOrNull()
    }

    private fun createVariables(input: PlanningInput) = input.jobs

    private fun getDomainForJob(
        job: Job,
        currentTime: Time,
        timeStep: Time,
        jobAssignment: Map<Time, Map<Job, JobAssignment>>,
        resourcesStack: Collection<ResourcesStack>
    ): Collection<ResourcesStack> {
        val jobData = getDataPerJob(job, jobAssignment)[job]
//        var maxPrice = null as Cost?

        if (jobData != null) {
            if (timeStep * jobData.size >= job.parameters.maxTime) return emptyList()

            val currentPrice = jobData.values.map { it.resourcesStack * it.memory + it.resourcesStack * it.cpu }.sumOnlyValues()
            if (currentPrice >= job.parameters.maxCost) return emptyList()
//            maxPrice = job.parameters.maxCost - currentPrice //TODO max price?
        }

        val assignment = jobAssignment[currentTime - timeStep]?.get(job) ?: return resourcesStack
        return resourcesStack.filter { it isFor assignment.resourcesStack }
    }

    private fun getDataPerJob(job: Job, jobAssignment: Map<Time, Map<Job, JobAssignment>>) =
        jobAssignment.swapKeys()

    private data class ResourcesAllocation(val cpu: CpuResources, val memory: MemoryResources)
}
