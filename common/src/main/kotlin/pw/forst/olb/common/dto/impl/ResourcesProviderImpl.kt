package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.resources.CpuCost
import pw.forst.olb.common.dto.resources.MemoryCost
import pw.forst.olb.common.dto.resources.ResourcesProvider
import java.util.UUID

data class ResourcesProviderImpl(
    override val uuid: UUID,
    override val cpuCost: CpuCost,
    override val memoryCost: MemoryCost,
    override val name: String = uuid.toString()
) : ResourcesProvider {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResourcesProviderImpl

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    override fun toString(): String {
        return "ResourcesProviderImpl(name='$name')"
    }
}
