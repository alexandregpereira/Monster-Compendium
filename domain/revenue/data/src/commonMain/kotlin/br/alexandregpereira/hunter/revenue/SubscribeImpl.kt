package br.alexandregpereira.hunter.revenue

internal class SubscribeImpl(
    private val revenueSdk: RevenueSdk,
) : Subscribe {

    override suspend fun invoke() {
        revenueSdk.purchase()
    }
}
