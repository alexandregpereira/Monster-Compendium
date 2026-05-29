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

package br.alexandregpereira.hunter.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.UIKitView
import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import cocoapods.Google_Mobile_Ads_SDK.GADBannerView
import cocoapods.Google_Mobile_Ads_SDK.GADLargeAnchoredAdaptiveBannerAdSizeWithWidth
import cocoapods.Google_Mobile_Ads_SDK.GADRequest
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.koin.compose.koinInject
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIScreen
import platform.UIKit.UIWindowScene

@OptIn(ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)
@Composable
internal actual fun AdsBannerView() {
    val consentManager: AdsConsentManager = koinInject()
    val canRequestAds by consentManager.canRequestAds.collectAsState()
    val adLoaded = remember { mutableStateOf(false) }

    // UIKitView is always composed so factory runs at app startup when the scene is stable.
    // loadRequest is deferred to update() to avoid a timing issue where the scene is briefly
    // inactive during the UMP/ATT consent modal dismissal animation.
    UIKitView(
        factory = {
            val scene = UIApplication.sharedApplication.connectedScenes
                .filterIsInstance<UIWindowScene>()
                .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
            val rootViewController = scene?.keyWindow?.rootViewController
                ?: return@UIKitView GADBannerView()

            val screenWidth = UIScreen.mainScreen.bounds.useContents { size.width }
            val adSize = GADLargeAnchoredAdaptiveBannerAdSizeWithWidth(screenWidth)
            GADBannerView(adSize).apply {
                adUnitID = if (Platform.isDebugBinary) {
                    "ca-app-pub-3940256099942544/2435281174"
                } else {
                    "ca-app-pub-9186388258407371/2524216431"
                }
                this.rootViewController = rootViewController
            }
        },
        update = { bannerView ->
            if (canRequestAds && !adLoaded.value && bannerView.adUnitID != null) {
                bannerView.loadRequest(GADRequest.request())
                adLoaded.value = true
            }
        },
    )
}
