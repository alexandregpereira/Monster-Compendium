package br.alexandregpereira.hunter.revenue

internal class GetCurrentOfferImpl(
    private val revenueSdk: RevenueSdk,
) : GetCurrentOffer {

    override suspend fun invoke(): Offer {
        return revenueSdk.getOffers().firstOrNull() ?: throw GetCurrentOfferException.OfferNotFound(
            internalMessage = "Offer is null"
        )
    }
}
