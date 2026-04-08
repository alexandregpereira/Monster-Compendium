package br.alexandregpereira.hunter.ads.consent.di

import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun adsConsentCoreModule(deviceDebugHashTestId: String? = null) = module {
    single<AdsConsentManager> {
        createAdsConsentManager(deviceDebugHashTestId)
    }
}

internal expect fun Scope.createAdsConsentManager(deviceDebugHashTestId: String?): AdsConsentManager
