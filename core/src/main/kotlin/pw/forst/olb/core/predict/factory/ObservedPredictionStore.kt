package pw.forst.olb.core.predict.factory

import java.util.UUID

data class ObservedPredictionStore(
    private val data: Map<UUID, Map<UUID, CachedPrediction>>
) : PredictionStore {

    private val observers = mutableSetOf<PredictionStoreObserver>()

    override fun registerObserver(observer: PredictionStoreObserver) {
        observers.add(observer)
    }

    override fun hasPlan(planId: UUID): Boolean = data.containsKey(planId)

    override fun hasJob(planId: UUID, jobId: UUID): Boolean = data[planId]?.containsKey(jobId) ?: false

    override fun predictionFor(planId: UUID, jobId: UUID): CachedPrediction? = data[planId]?.get(jobId)

    override fun predictionFor(planId: UUID): Map<UUID, CachedPrediction>? = data[planId]

    override fun updateData(data: Map<UUID, Map<UUID, CachedPrediction>>): PredictionStore = copy(data = data).also { newData -> observers.forEach { it.dataUpdated(newData) } }
}
