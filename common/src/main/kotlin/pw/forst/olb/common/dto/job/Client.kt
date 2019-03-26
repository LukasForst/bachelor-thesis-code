package pw.forst.olb.common.dto.job

import java.util.UUID

interface Client {
    val name: String

    val uuid: UUID
}
