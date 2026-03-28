package br.alexandregpereira.hunter.revenue.di

import br.alexandregpereira.hunter.revenue.PaywallStateHolder
import br.alexandregpereira.hunter.revenue.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.revenue.event.RevenueEventDispatcher
import br.alexandregpereira.hunter.revenue.event.RevenueResultDispatcher
import org.koin.dsl.module

val revenueFeatureModule = module {
    factory {
        ShouldShowPaywall(
            isSessionUsageLimitReached = get(),
            networkManager = get(),
        )
    }
    single {
        PaywallStateHolder(
            revenueEventListener = get<RevenueEventDispatcher>(),
            shouldShowPaywall = get(),
            revenueResultDispatcher = get<RevenueResultDispatcher>(),
        )
    }
    single {
        RevenueEventDispatcher()
    }
    single {
        RevenueResultDispatcher()
    }
}
