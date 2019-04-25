package pw.forst.olb.scheduler.client

import org.koin.dsl.module.module
import pw.forst.olb.common.config.REMOTE_SCHEDULER_API
import pw.forst.olb.common.config.SCHEDULING_SERVER_API
import pw.forst.olb.scheduler.client.scenarios.PeriodicConfiguration
import pw.forst.olb.scheduler.client.scenarios.RemotePlanningRound

val serverModule = module {
    // response handler, need http client
    single { RequestSender(get(), "$SCHEDULING_SERVER_API/scheduling") }

    // scheduling queue, needs core API and executor service
    single { SchedulingQueue(get(), "$REMOTE_SCHEDULER_API/response") }

    // scheduling core
    single { RemoteOlbApi(get()) }

    // scenarios
    single { RemotePlanningRound(get()) }
    single { PeriodicConfiguration(get()) }
}
