package br.alexandregpereira.hunter.analytics.di

import br.alexandregpereira.hunter.analytics.Analytics
import org.koin.core.scope.Scope
import org.koin.dsl.module

val analyticsModule = module {
    single { createAnalytics() }
}

internal expect fun Scope.createAnalytics(): Analytics
