package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.job.Client
import java.util.UUID

data class SimpleClient(
    override val uuid: UUID,
    override val name: String = uuid.toString()
) : Client
