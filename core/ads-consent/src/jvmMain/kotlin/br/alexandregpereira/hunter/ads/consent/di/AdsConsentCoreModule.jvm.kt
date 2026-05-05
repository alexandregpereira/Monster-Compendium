package br.alexandregpereira.hunter.ads.consent.di

import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import br.alexandregpereira.hunter.ads.consent.EmptyAdsConsentManager
import org.koin.core.scope.Scope

internal actual fun Scope.createAdsConsentManager(
    deviceDebugHashTestId: String?
): AdsConsentManager {
    return EmptyAdsConsentManager()
}
