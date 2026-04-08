package br.alexandregpereira.hunter.ads.consent

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.alexandregpereira.hunter.analytics.Analytics
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@SuppressLint("MissingPermission")
class AndroidAdsConsentManager(
    private val context: Context,
    private val analytics: Analytics,
    private val debugHashedId: String? = null,
) : AdsConsentManager {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    private val _canRequestAds = MutableStateFlow(consentInformation.canRequestAds())
    override val canRequestAds: StateFlow<Boolean> = _canRequestAds.asStateFlow()

    private var resumedActivity: Activity? = null

    override fun initialize() {
        MobileAds.initialize(context)
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityResumed(activity: Activity) {
                    resumedActivity = activity
                }
                override fun onActivityPaused(activity: Activity) {
                    resumedActivity = null
                }
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            }
        )
    }

    override fun showConsentFormIfRequired() {
        requestConsent(shouldShowConsentForm = true)
    }

    override fun loadConsentInfo() {
        requestConsent(shouldShowConsentForm = false)
    }

    private fun requestConsent(shouldShowConsentForm: Boolean) {
        val activity = resumedActivity
        if (activity == null) {
            analytics.logException(IllegalStateException("Failed to check ads consent. resumedActivity is null."))
            return
        }
        Handler(Looper.getMainLooper()).post {
            if (shouldShowConsentForm) {
                requestConsentInfoUpdate(activity) {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                        _canRequestAds.value = consentInformation.canRequestAds()
                    }
                }
            } else {
                requestConsentInfoUpdate(activity) {
                    _canRequestAds.value = consentInformation.canRequestAds()
                }
            }
        }
    }

    private fun requestConsentInfoUpdate(activity: Activity, onSuccess: () -> Unit) {
        val params = buildParams(activity)
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            { onSuccess() },
            { _canRequestAds.value = consentInformation.canRequestAds() },
        )
    }

    private fun buildParams(activity: Activity): ConsentRequestParameters {
        val debugSettings = debugHashedId?.let {
            ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId(it)
                .build()
        }
        return ConsentRequestParameters.Builder()
            .apply { debugSettings?.let { setConsentDebugSettings(it) } }
            .build()
    }
}
