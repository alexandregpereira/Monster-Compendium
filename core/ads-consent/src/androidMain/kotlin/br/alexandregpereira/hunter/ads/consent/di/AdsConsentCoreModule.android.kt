package br.alexandregpereira.hunter.ads.consent.di

import android.app.Application
import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import br.alexandregpereira.hunter.ads.consent.AndroidAdsConsentManager
import org.koin.core.scope.Scope

internal actual fun Scope.createAdsConsentManager(
    deviceDebugHashTestId: String?
): AdsConsentManager {
    return AndroidAdsConsentManager(
        context = get<Application>(),
        analytics = get(),
        debugHashedId = deviceDebugHashTestId,
    )
}
