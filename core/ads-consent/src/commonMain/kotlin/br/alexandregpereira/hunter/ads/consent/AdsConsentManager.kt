package br.alexandregpereira.hunter.ads.consent

import kotlinx.coroutines.flow.StateFlow

interface AdsConsentManager {
    val canRequestAds: StateFlow<Boolean>

    fun initialize()

    fun showConsentFormIfRequired()
    fun loadConsentInfo()
}
