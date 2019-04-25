package pw.forst.olb.common.dto.job

import java.io.Serializable
import java.util.UUID

interface Client : Serializable {
    val name: String

    val uuid: UUID
}
