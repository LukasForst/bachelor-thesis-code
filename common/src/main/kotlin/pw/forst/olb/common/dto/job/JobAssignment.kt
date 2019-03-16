package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.Cost
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.ResourcesAllocation

interface JobAssignment {

    val job: Job

    val time: Time

    val allocation: ResourcesAllocation

    val cost: Cost
}
