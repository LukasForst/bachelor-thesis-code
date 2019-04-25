package pw.forst.olb.common.dto.server

import java.io.Serializable

interface SchedulingRequest : Serializable {
    val reportEndpoint: ReportEndpoint
}
