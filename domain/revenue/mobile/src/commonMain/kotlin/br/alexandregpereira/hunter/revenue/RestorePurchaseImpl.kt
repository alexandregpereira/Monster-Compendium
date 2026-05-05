package br.alexandregpereira.hunter.revenue

internal class RestorePurchaseImpl(
    private val revenueSdk: RevenueSdk,
    private val isPremium: IsPremium,
) : RestorePurchase {

    override suspend fun invoke() {
        if (isPremium(ignoreCache = true)) return
        revenueSdk.restorePurchase()
    }
}
