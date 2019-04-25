package pw.forst.olb.common.dto.job

import java.io.Serializable
import java.util.UUID

interface Job : Serializable {
    val parameters: JobParameters

    val client: Client

    val name: String

    val uuid: UUID
}


