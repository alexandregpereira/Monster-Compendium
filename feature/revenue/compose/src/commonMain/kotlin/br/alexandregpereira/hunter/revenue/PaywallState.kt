package br.alexandregpereira.hunter.revenue

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class PaywallState(
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val features: ImmutableList<PaywallFeatureState> = persistentListOf(),
    val offer: PaywallOfferState = PaywallOfferState.Success(),
    val strings: PaywallStrings = PaywallStrings(),
)

internal sealed class PaywallOfferState {
    data class Success(
        val subscriptionOfferFormatted: String = "",
    ) : PaywallOfferState()

    data object UnknownError : PaywallOfferState()
}

internal data class PaywallFeatureState(
    val name: String = "",
    val isPremium: Boolean = false,
)
