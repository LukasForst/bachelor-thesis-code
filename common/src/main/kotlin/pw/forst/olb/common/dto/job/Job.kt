package pw.forst.olb.common.dto.job

import java.util.UUID

interface Job {
    val parameters: JobParameters

    val client: Client

    val name: String

    val uuid: UUID
}


