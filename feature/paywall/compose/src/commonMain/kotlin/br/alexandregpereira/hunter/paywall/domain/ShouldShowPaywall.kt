package br.alexandregpereira.hunter.paywall.domain

import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.network.NetworkManager
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached
import kotlinx.coroutines.flow.firstOrNull

internal class ShouldShowPaywall(
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
    private val networkManager: NetworkManager,
    private val getCurrentOffer: GetCurrentOffer,
    private val settings: PaywallSettings,
    private val analytics: Analytics,
    private val adsConsentManager: AdsConsentManager,
) {
    suspend operator fun invoke(): Boolean {
        val paywallWasNotClosed = settings.getPaywallWasClosedFlag().not()
        val shouldShow = paywallWasNotClosed && isSessionUsageLimitReached() &&
                networkManager.isNetworkAvailable() &&
                isCurrentOfferAvailable() &&
                canRequestAds()
        return shouldShow
    }

    private suspend fun canRequestAds(): Boolean {
        if (adsConsentManager.canRequestAds.value) {
            return true
        }
        val canRequestAds = adsConsentManager.canRequestAds.firstOrNull { canRequestAds ->
            canRequestAds
        }
        return canRequestAds ?: false
    }

    private suspend fun isCurrentOfferAvailable(): Boolean {
        return try{
            getCurrentOffer()
            true
        } catch (cause: Throwable) {
            val exception = ShouldShowPaywallException(
                message = "Failed to get the current offer on ShouldShowPaywall",
                cause = cause,
            )
            analytics.logException(exception)
            false
        }
    }
}

private class ShouldShowPaywallException(message: String, cause: Throwable) : Throwable(message, cause)
