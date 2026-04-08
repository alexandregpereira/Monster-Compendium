package br.alexandregpereira.hunter.ads.consent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class EmptyAdsConsentManager : AdsConsentManager {
    override val canRequestAds: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
    override fun initialize() {}
    override fun showConsentFormIfRequired() {}
    override fun loadConsentInfo() {}
}
