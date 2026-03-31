package br.alexandregpereira.hunter.revenue.di

import br.alexandregpereira.hunter.revenue.Offer
import br.alexandregpereira.hunter.revenue.RevenueSdk

internal actual fun createRevenueSdk(): RevenueSdk {
    return EmptyRevenueSdk()
}

private class EmptyRevenueSdk(): RevenueSdk {
    override fun initialize(apiKey: String) {}

    override suspend fun isPremiumEnabled(): Boolean = true

    override suspend fun purchase(offerId: String) {
        throw NotImplementedError("Purchase is not implemented in iOS yet")
    }

    override suspend fun restorePurchase() {
        throw NotImplementedError("Restore purchase is not implemented in iOS yet")
    }

    override suspend fun getOffers(): List<Offer> {
        throw NotImplementedError("Get offers is not implemented in iOS yet")
    }
}
