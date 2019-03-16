package pw.forst.olb.common.dto.scheduling

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.job.JobAssignment
import pw.forst.olb.common.dto.job.JobValue


interface JobPlanView {

    val job: Job

    val totalSchedulingTime: Time

    val totalCost: Cost

    val assignments: Map<Time, JobAssignment>

    val values: Map<Time, JobValue>

    val timeStep: Time
}
