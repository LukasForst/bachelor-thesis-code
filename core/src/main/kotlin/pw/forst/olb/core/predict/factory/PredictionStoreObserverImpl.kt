package pw.forst.olb.core.predict.factory

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class PredictionStoreObserverImpl(
    private var store: PredictionStore
) : PredictionStoreObserver {

    private val lock: Lock = ReentrantLock()

    init {
        store.registerObserver(this)
    }

    override fun obtainStore(): PredictionStore? = lock.withLock { store }

    override fun getStore(): PredictionStore = obtainStore()!!

    override fun dataUpdated(new: PredictionStore) = lock.withLock { store = new }
}
