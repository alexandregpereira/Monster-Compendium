package br.alexandregpereira.hunter.ads.consent

import br.alexandregpereira.hunter.analytics.Analytics
import cocoapods.GoogleUserMessagingPlatform.UMPConsentForm
import cocoapods.GoogleUserMessagingPlatform.UMPConsentInformation
import cocoapods.GoogleUserMessagingPlatform.UMPDebugGeographyEEA
import cocoapods.GoogleUserMessagingPlatform.UMPDebugSettings
import cocoapods.GoogleUserMessagingPlatform.UMPRequestParameters
import cocoapods.Google_Mobile_Ads_SDK.GADMobileAds
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSError
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
internal class IosAdsConsentManager(
    private val analytics: Analytics,
    private val debugHashedId: String? = null,
) : AdsConsentManager {

    private val consentInformation = UMPConsentInformation.sharedInstance

    private val _canRequestAds = MutableStateFlow(consentInformation.canRequestAds)
    override val canRequestAds: StateFlow<Boolean> = _canRequestAds.asStateFlow()

    override fun initialize() {
        GADMobileAds.sharedInstance().startWithCompletionHandler(null)
    }

    override fun showConsentFormIfRequired() {
        requestConsent(shouldShowConsentForm = true)
    }

    override fun loadConsentInfo() {
        requestConsent(shouldShowConsentForm = false)
    }

    private fun requestConsent(shouldShowConsentForm: Boolean) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        if (rootViewController == null) {
            analytics.logException(
                IllegalStateException("Failed to check ads consent. rootViewController is null.")
            )
            return
        }

        val params = UMPRequestParameters()
        debugHashedId?.let { hashedId ->
            val debugSettings = UMPDebugSettings()
            debugSettings.testDeviceIdentifiers = listOf(hashedId)
            debugSettings.geography = UMPDebugGeographyEEA
            params.debugSettings = debugSettings
        }
        consentInformation.requestConsentInfoUpdateWithParameters(params) { error: NSError? ->
            if (error != null) {
                analytics.logException(Exception(error.localizedDescription))
                _canRequestAds.value = consentInformation.canRequestAds
                return@requestConsentInfoUpdateWithParameters
            }
            if (shouldShowConsentForm) {
                UMPConsentForm.loadAndPresentIfRequiredFromViewController(
                    rootViewController
                ) { _: NSError? ->
                    _canRequestAds.value = consentInformation.canRequestAds
                }
            } else {
                _canRequestAds.value = consentInformation.canRequestAds
            }
        }
    }
}
