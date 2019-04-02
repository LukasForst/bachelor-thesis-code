package pw.forst.olb.core.predict.factory

import java.util.UUID

interface PredictionStore {

    fun hasPlan(planId: UUID): Boolean

    fun hasJob(planId: UUID, jobId: UUID): Boolean

    fun predictionFor(planId: UUID, jobId: UUID): CachedPrediction?

    fun predictionFor(planId: UUID): Map<UUID, CachedPrediction>?

    fun updateData(data: Map<UUID, Map<UUID, CachedPrediction>>): PredictionStore

    fun registerObserver(observer: PredictionStoreObserver)
}
