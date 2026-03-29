package br.alexandregpereira.hunter.paywall.di

import br.alexandregpereira.hunter.paywall.PaywallStateHolder
import br.alexandregpereira.hunter.paywall.domain.PaywallSettings
import br.alexandregpereira.hunter.paywall.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.paywall.event.PaywallEventDispatcher
import br.alexandregpereira.hunter.paywall.event.PaywallResultDispatcher
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import org.koin.dsl.module

val revenueFeatureModule = module {
    factory {
        ShouldShowPaywall(
            isSessionUsageLimitReached = get(),
            networkManager = get(),
        )
    }
    factory {
        PaywallSettings(
            settings = get(),
            dispatcher = get(),
        )
    }
    single {
        PaywallStateHolder(
            paywallEventListener = get<PaywallEventDispatcher>(),
            shouldShowPaywall = get(),
            paywallResultDispatcher = get<PaywallResultDispatcher>(),
            settings = get(),
            purchase = get(),
            getCurrentOffer = get<GetCurrentOffer>(),
            restorePurchase = get(),
            appLocalization = get(),
        )
    }
    single {
        PaywallEventDispatcher()
    }
    single {
        PaywallResultDispatcher()
    }
}
