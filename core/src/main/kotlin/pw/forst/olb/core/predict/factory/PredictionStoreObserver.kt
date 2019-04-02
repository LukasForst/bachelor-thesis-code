package pw.forst.olb.core.predict.factory

interface PredictionStoreObserver {

    fun dataUpdated(new: PredictionStore)

    fun obtainStore(): PredictionStore?

    fun getStore(): PredictionStore
}
