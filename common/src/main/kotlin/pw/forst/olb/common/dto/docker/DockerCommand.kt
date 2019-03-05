package pw.forst.olb.common.dto.docker

sealed class DockerCommand

data class DockerJobSendDataCommand(val cmds: Array<String>) : DockerCommand() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DockerJobSendDataCommand

        if (!cmds.contentEquals(other.cmds)) return false

        return true
    }

    override fun hashCode(): Int {
        return cmds.contentHashCode()
    }
}
