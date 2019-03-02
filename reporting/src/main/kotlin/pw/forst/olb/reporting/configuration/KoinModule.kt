package pw.forst.olb.reporting.configuration

import org.koin.dsl.module.module
import pw.forst.olb.reporting.HelloService

val reportingModule = module {
    single { HelloService() }
}
