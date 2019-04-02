package pw.forst.olb.core.predict.factory

object PredictionStoreFactory {

    private val predictionStoreObserver: PredictionStoreObserver = PredictionStoreObserverImpl(ObservedPredictionStore(emptyMap()))

    fun getStore(): PredictionStore = predictionStoreObserver.getStore()

    fun addToStore(data: Collection<CachedPrediction>): PredictionStore =
        getStore().updateData(
            data.groupBy { it.job.plan.uuid }
                .mapValues { (_, planData) ->
                    planData.associateBy { it.job.uuid }
                }
        )

}
