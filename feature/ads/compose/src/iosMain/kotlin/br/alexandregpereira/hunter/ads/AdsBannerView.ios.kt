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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.Google_Mobile_Ads_SDK.GADBannerView
import cocoapods.Google_Mobile_Ads_SDK.GADBannerViewDelegateProtocol
import cocoapods.Google_Mobile_Ads_SDK.GADLargeAnchoredAdaptiveBannerAdSizeWithWidth
import cocoapods.Google_Mobile_Ads_SDK.GADRequest
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIScreen
import platform.UIKit.UIWindowScene
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)
@Composable
internal actual fun AdsBannerView(
    onAdLoaded: () -> Unit,
    onAdImpression: () -> Unit,
    onAdFailedToLoad: (errorCode: Int?, errorMessage: String?) -> Unit,
) {
    val adRequested = remember { mutableStateOf(false) }
    val currentOnAdLoaded by rememberUpdatedState(onAdLoaded)
    val currentOnAdImpression by rememberUpdatedState(onAdImpression)
    val currentOnAdFailedToLoad by rememberUpdatedState(onAdFailedToLoad)

    // GADBannerView holds the delegate weakly, so it has to be kept alive by the composition.
    val delegate = remember {
        object : NSObject(), GADBannerViewDelegateProtocol {
            override fun bannerViewDidReceiveAd(bannerView: GADBannerView) {
                currentOnAdLoaded()
            }

            override fun bannerViewDidRecordImpression(bannerView: GADBannerView) {
                currentOnAdImpression()
            }

            override fun bannerView(
                bannerView: GADBannerView,
                didFailToReceiveAdWithError: NSError,
            ) {
                currentOnAdFailedToLoad(
                    didFailToReceiveAdWithError.code.toInt(),
                    didFailToReceiveAdWithError.localizedDescription,
                )
            }
        }
    }

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
                this.delegate = delegate
            }
        },
        update = { bannerView ->
            if (adRequested.value.not()) {
                adRequested.value = true
                if (bannerView.adUnitID == null) {
                    // There was no active scene when the banner was created.
                    currentOnAdFailedToLoad(null, "No active scene to attach the banner to")
                } else {
                    bannerView.loadRequest(GADRequest.request())
                }
            }
        },
    )
}
