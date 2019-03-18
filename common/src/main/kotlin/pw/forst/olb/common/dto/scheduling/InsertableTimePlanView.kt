package pw.forst.olb.common.dto.scheduling

import pw.forst.olb.common.dto.job.JobAssignment


interface InsertableTimePlanView : TimePlanView {

    fun insert(jobAssignment: JobAssignment): InsertableTimePlanView
}
