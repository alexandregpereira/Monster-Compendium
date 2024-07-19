package br.alexandregpereira.hunter.app.event

import org.koin.dsl.module

val appEventModule = module {
    single<AppEventDispatcher> { AppEventDispatcherImpl(get(), get()) }
}
