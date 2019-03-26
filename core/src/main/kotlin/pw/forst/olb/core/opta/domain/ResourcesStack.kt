package pw.forst.olb.core.opta.domain

data class ResourcesStack(
    val id: Long = 0L,

    val cpus: Double = 0.0
) {
    companion object {
        val empty: ResourcesStack = ResourcesStack()
    }
}
