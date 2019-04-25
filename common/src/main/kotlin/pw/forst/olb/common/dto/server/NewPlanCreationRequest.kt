package pw.forst.olb.common.dto.server

import pw.forst.olb.common.dto.SchedulingInput
import java.io.Serializable

data class NewPlanCreationRequest(
    val input: SchedulingInput,
    override val reportEndpoint: ReportEndpoint
) : SchedulingRequest, Serializable
