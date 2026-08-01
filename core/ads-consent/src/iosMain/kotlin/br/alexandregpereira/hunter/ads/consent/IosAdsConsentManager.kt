/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
import platform.AppTrackingTransparency.ATTrackingManager
import platform.AppTrackingTransparency.ATTrackingManagerAuthorizationStatusAuthorized
import platform.AppTrackingTransparency.ATTrackingManagerAuthorizationStatusNotDetermined
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIWindowScene
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
internal class IosAdsConsentManager(
    private val analytics: Analytics,
    private val debugHashedId: String? = null,
) : AdsConsentManager {

    private val consentInformation = UMPConsentInformation.sharedInstance

    private val _canRequestAds = MutableStateFlow(false)
    override val canRequestAds: StateFlow<Boolean> = _canRequestAds.asStateFlow()

    private var adsSdkStarted = false

    // Both onStart and onResume trigger a consent check, and the App Tracking Transparency alert
    // itself causes a resign/become active cycle that triggers another one. Without this guard the
    // UMP form and the ATT prompt end up presented at the same time.
    private var consentRequestInProgress = false

    // The Google Mobile Ads SDK must not start before the user answers the App Tracking
    // Transparency prompt, otherwise it loads ad web content and sets tracking cookies while the
    // authorization status is still undetermined. It is started in finishConsent() instead.
    override fun initialize() = Unit

    override fun showConsentFormIfRequired() {
        requestConsent(shouldShowConsentForm = true)
    }

    override fun loadConsentInfo() {
        requestConsent(shouldShowConsentForm = false)
    }

    private fun requestConsent(shouldShowConsentForm: Boolean) {
        if (consentRequestInProgress) return

        val scene = UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
        val rootViewController = scene?.keyWindow?.rootViewController
        if (rootViewController == null) {
            analytics.logException(
                IllegalStateException("Failed to check ads consent. rootViewController is null.")
            )
            return
        }

        consentRequestInProgress = true
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
                if (shouldShowConsentForm) {
                    onConsentResolved()
                } else {
                    publishConsentState()
                }
                return@requestConsentInfoUpdateWithParameters
            }
            if (shouldShowConsentForm) {
                UMPConsentForm.loadAndPresentIfRequiredFromViewController(
                    rootViewController
                ) { _: NSError? ->
                    onConsentResolved()
                }
            } else {
                // Ads are not going to be shown, so there is no reason to ask for tracking
                // authorization or to start the ads SDK.
                publishConsentState()
            }
        }
    }

    private fun onConsentResolved() {
        if (ATTrackingManager.trackingAuthorizationStatus !=
            ATTrackingManagerAuthorizationStatusNotDetermined
        ) {
            finishConsent()
            return
        }
        // Give the consent form time to finish dismissing. The ATT alert only presents when no
        // other modal owns the window, so requesting it too early can make it never appear.
        dispatch_async(dispatch_get_main_queue()) {
            ATTrackingManager.requestTrackingAuthorizationWithCompletionHandler { _ ->
                finishConsent()
            }
        }
    }

    private fun finishConsent() {
        applyPublisherFirstPartyIdSetting()
        if (consentInformation.canRequestAds) {
            startAdsSdkIfNeeded()
        }
        publishConsentState()
    }

    /**
     * The Google Mobile Ads SDK publisher first-party ID is enabled by default and persists across
     * app sessions, which means it keeps identifying the user through ad request cookies even after
     * tracking is denied. It has to be reapplied on every launch, before the SDK starts.
     */
    private fun applyPublisherFirstPartyIdSetting() {
        val trackingAuthorized = ATTrackingManager.trackingAuthorizationStatus ==
            ATTrackingManagerAuthorizationStatusAuthorized
        GADMobileAds.sharedInstance().requestConfiguration
            .setPublisherFirstPartyIDEnabled(trackingAuthorized)
    }

    private fun publishConsentState() {
        consentRequestInProgress = false
        _canRequestAds.value = consentInformation.canRequestAds
    }

    private fun startAdsSdkIfNeeded() {
        if (adsSdkStarted) return
        adsSdkStarted = true
        GADMobileAds.sharedInstance().startWithCompletionHandler(null)
    }
}
