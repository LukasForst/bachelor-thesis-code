package pw.forst.olb.common.dto.server

import java.io.Serializable

data class ReportEndpoint(
    val method: String,
    val url: String
) : Serializable
