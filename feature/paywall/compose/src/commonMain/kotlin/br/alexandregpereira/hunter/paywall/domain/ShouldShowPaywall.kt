package br.alexandregpereira.hunter.paywall.domain

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.network.NetworkManager
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached

internal class ShouldShowPaywall(
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
    private val networkManager: NetworkManager,
    private val getCurrentOffer: GetCurrentOffer,
    private val settings: PaywallSettings,
    private val analytics: Analytics,
) {
    suspend operator fun invoke(): Boolean {
        val paywallWasNotClosed = settings.getPaywallWasClosedFlag().not()
        return paywallWasNotClosed && isSessionUsageLimitReached() &&
                networkManager.isNetworkAvailable() &&
                isCurrentOfferAvailable()
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
