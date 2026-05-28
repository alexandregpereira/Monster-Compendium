package br.alexandregpereira.hunter.ads

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.koin.compose.koinInject

@SuppressLint("MissingPermission")
@Composable
internal actual fun AdsBannerView() {
    val consentManager: AdsConsentManager = koinInject()
    val canRequestAds by consentManager.canRequestAds.collectAsState()
    val context = LocalContext.current
    val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    if (!canRequestAds) return

    AndroidView(
        factory = { ctx ->
            val displayMetrics = ctx.resources.displayMetrics
            val adWidthPixels = displayMetrics.widthPixels
            val density = displayMetrics.density
            val adWidthDp = (adWidthPixels / density).toInt()

            AdView(ctx).apply {
                setAdSize(AdSize.getLargeAnchoredAdaptiveBannerAdSize(ctx, adWidthDp))
                adUnitId = if (isDebug) {
                    "ca-app-pub-3940256099942544/9214589741"
                } else {
                    "ca-app-pub-9186388258407371/4662481578"
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                loadAd(AdRequest.Builder().build())
            }
        },
    )
}
