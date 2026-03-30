package br.alexandregpereira.hunter.paywall

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class PaywallState(
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val features: ImmutableList<PaywallFeatureState> = persistentListOf(),
    val subscriptionOfferFormatted: String = "",
    val strings: PaywallStrings = PaywallStrings(),
    val errorState: PaywallErrorState? = null,
)

internal data class PaywallFeatureState(
    val name: String = "",
    val isPremium: Boolean = false,
)

internal sealed class PaywallErrorState {
    data object GetCurrentOfferError : PaywallErrorState()
    data object PurchaseError : PaywallErrorState()
    data object RestorePurchaseError : PaywallErrorState()
}
