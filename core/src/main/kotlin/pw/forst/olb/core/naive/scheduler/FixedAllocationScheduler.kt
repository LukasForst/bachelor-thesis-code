package pw.forst.olb.core.naive.scheduler

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignmentImpl
import pw.forst.olb.common.dto.planning.PlanningInput
import pw.forst.olb.common.dto.planning.SimplePlan
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.step
import pw.forst.olb.common.dto.until
import pw.forst.olb.core.Scheduler

class FixedAllocationScheduler : Scheduler {
    override fun createPlan(input: PlanningInput): SimplePlan {
        val finalMap = sortedMapOf<Time, Map<Job, JobAssignmentImpl>>()

        for (time in input.currentTime until input.planHorizon step input.timeStep) {
            val assignments = mutableMapOf<Job, JobAssignmentImpl>()


            var resourcesStack = input.resourcesStack.first()
            for (job in input.jobs) {
                val cpu = CpuResources(1.0, CpuPowerType.SINGLE_CORE)
                val mem = MemoryResources(1024)

                val jobAssignment = JobAssignmentImpl(
                    job = job,
                    time = time,
                    cpu = cpu,
                    memory = mem,
                    resourcesStack = resourcesStack
                )

                resourcesStack -= cpu
                resourcesStack -= mem

                assignments[job] = jobAssignment
            }

            finalMap[time] = assignments
        }

        return SimplePlan(assignments = finalMap)
    }

}
