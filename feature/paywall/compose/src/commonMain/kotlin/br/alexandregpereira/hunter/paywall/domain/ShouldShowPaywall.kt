package br.alexandregpereira.hunter.paywall.domain

import br.alexandregpereira.hunter.network.NetworkManager
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached

internal class ShouldShowPaywall(
    private val isSessionUsageLimitReached: IsSessionUsageLimitReached,
    private val networkManager: NetworkManager,
) {
    suspend operator fun invoke(): Boolean {
        return isSessionUsageLimitReached() && networkManager.isNetworkAvailable()
    }
}
