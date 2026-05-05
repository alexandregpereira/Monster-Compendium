package br.alexandregpereira.hunter.revenue

internal class PurchaseImpl(
    private val revenueSdk: RevenueSdk,
) : Purchase {

    override suspend fun invoke(offerId: String) {
        revenueSdk.purchase(offerId)
    }
}
