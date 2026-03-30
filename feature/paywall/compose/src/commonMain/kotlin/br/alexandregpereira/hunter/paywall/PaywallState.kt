package br.alexandregpereira.hunter.paywall

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class PaywallState(
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val features: ImmutableList<PaywallFeatureState> = persistentListOf(),
    val subscriptionOfferFormatted: String = "",
    val strings: PaywallStrings = PaywallStrings(),
    val actionResultState: PaywallActionResultState? = null,
)

internal data class PaywallFeatureState(
    val name: String = "",
    val isPremium: Boolean = false,
)

internal sealed class PaywallActionResultState {
    data object Success : PaywallActionResultState()
    data object GetCurrentOfferError : PaywallActionResultState()
    data object PurchaseError : PaywallActionResultState()
    data object RestorePurchaseError : PaywallActionResultState()
}
