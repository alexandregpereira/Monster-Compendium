package br.alexandregpereira.hunter.ads.di

import br.alexandregpereira.hunter.ads.AdsStateHolder
import br.alexandregpereira.hunter.paywall.event.PaywallResultDispatcher
import org.koin.dsl.module

val adsFeatureModule = module {
    factory {
        AdsStateHolder(
            isSessionUsageLimitReached = get(),
            paywallResultListener = get<PaywallResultDispatcher>(),
            analytics = get(),
        )
    }
}
