package br.alexandregpereira.hunter.revenue

internal class RestorePurchaseImpl(
    private val revenueSdk: RevenueSdk,
) : RestorePurchase {

    override suspend fun invoke() {
        revenueSdk.restorePurchase()
    }
}
