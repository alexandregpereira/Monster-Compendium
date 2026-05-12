package br.alexandregpereira.hunter.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.UIKitView
import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import cocoapods.Google_Mobile_Ads_SDK.GADBannerView
import cocoapods.Google_Mobile_Ads_SDK.GADLargeAnchoredAdaptiveBannerAdSizeWithWidth
import cocoapods.Google_Mobile_Ads_SDK.GADRequest
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.koin.compose.koinInject
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)
@Composable
internal actual fun AdsBannerView() {
    val consentManager: AdsConsentManager = koinInject()
    val canRequestAds by consentManager.canRequestAds.collectAsState()

    if (!canRequestAds) return

    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: return

    UIKitView(
        factory = {
            val screenWidth = UIScreen.mainScreen.bounds.useContents { size.width }
            val adSize = GADLargeAnchoredAdaptiveBannerAdSizeWithWidth(screenWidth)
            GADBannerView(adSize).apply {
                adUnitID = if (Platform.isDebugBinary) {
                    "ca-app-pub-3940256099942544/2435281174"
                } else {
                    "ca-app-pub-9186388258407371/2524216431"
                }
                this.rootViewController = rootViewController
                loadRequest(GADRequest.request())
            }
        },
    )
}
