package pw.forst.olb.core.opta.domain

data class Job(
    val id: Long = 0L,
    val maxTime: Long = 0L,
    val maxPrice: Double = 0.0,
    val minCpu: Double = 0.0
) {
    companion object {
        val empty: Job = Job()
    }
}
