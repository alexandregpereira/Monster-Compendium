package br.alexandregpereira.hunter.revenue.di

import br.alexandregpereira.hunter.revenue.PaywallStateHolder
import br.alexandregpereira.hunter.revenue.event.RevenueEventDispatcher
import org.koin.dsl.module

val revenueFeatureModule = module {
    single {
        PaywallStateHolder(
            revenueEventListener = get<RevenueEventDispatcher>(),
            isSessionUsageLimitReached = get(),
        )
    }
    single {
        RevenueEventDispatcher()
    }
}
