package pw.forst.olb.core.naive.scheduler

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.planning.Plan
import pw.forst.olb.common.dto.planning.PlanningInput
import pw.forst.olb.common.dto.resources.CpuPowerType
import pw.forst.olb.common.dto.resources.CpuResources
import pw.forst.olb.common.dto.resources.MemoryResources
import pw.forst.olb.common.dto.step
import pw.forst.olb.common.dto.until
import pw.forst.olb.core.Scheduler

class FixedAllocationScheduler : Scheduler {
    override fun createPlan(input: PlanningInput): Plan {
        val finalMap = sortedMapOf<Time, Map<Job, JobAssignment>>()

        for (time in input.currentTime until input.planHorizon step input.timeStep) {
            val assignments = mutableMapOf<Job, JobAssignment>()


            var resourcesStack = input.resourcesStack.first()
            for (job in input.jobs) {
                val cpu = CpuResources(1.0, CpuPowerType.SINGLE_CORE)
                val mem = MemoryResources(1024)

                val jobAssignment = JobAssignment(
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

        return Plan(assignments = finalMap)
    }

}
