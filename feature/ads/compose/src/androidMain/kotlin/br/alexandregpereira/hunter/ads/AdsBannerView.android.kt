package br.alexandregpereira.hunter.ads

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@SuppressLint("MissingPermission")
@Composable
internal actual fun AdsBannerView(
    onAdLoaded: () -> Unit,
    onAdImpression: () -> Unit,
    onAdFailedToLoad: (errorCode: Int?, errorMessage: String?) -> Unit,
) {
    val context = LocalContext.current
    val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    val currentOnAdLoaded by rememberUpdatedState(onAdLoaded)
    val currentOnAdImpression by rememberUpdatedState(onAdImpression)
    val currentOnAdFailedToLoad by rememberUpdatedState(onAdFailedToLoad)

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
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        currentOnAdLoaded()
                    }

                    override fun onAdImpression() {
                        currentOnAdImpression()
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        currentOnAdFailedToLoad(error.code, error.message)
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        },
        onRelease = { adView -> adView.destroy() },
    )
}
