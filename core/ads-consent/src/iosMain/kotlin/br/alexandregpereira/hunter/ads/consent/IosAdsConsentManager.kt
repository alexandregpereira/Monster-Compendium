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
import platform.AppTrackingTransparency.ATTrackingManagerAuthorizationStatusNotDetermined
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIWindowScene

@OptIn(ExperimentalForeignApi::class)
internal class IosAdsConsentManager(
    private val analytics: Analytics,
    private val debugHashedId: String? = null,
) : AdsConsentManager {

    private val consentInformation = UMPConsentInformation.sharedInstance

    private val _canRequestAds = MutableStateFlow(false)
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
                    _canRequestAds.value = consentInformation.canRequestAds
                }
                return@requestConsentInfoUpdateWithParameters
            }
            if (shouldShowConsentForm) {
                UMPConsentForm.loadAndPresentIfRequiredFromViewController(
                    rootViewController
                ) { _: NSError? ->
                    requestAttIfNeeded()
                }
            }
        }
    }

    private fun requestAttIfNeeded() {
        if (ATTrackingManager.trackingAuthorizationStatus ==
            ATTrackingManagerAuthorizationStatusNotDetermined
        ) {
            ATTrackingManager.requestTrackingAuthorizationWithCompletionHandler { _ ->
                _canRequestAds.value = consentInformation.canRequestAds
            }
        } else {
            _canRequestAds.value = consentInformation.canRequestAds
        }
    }
}
