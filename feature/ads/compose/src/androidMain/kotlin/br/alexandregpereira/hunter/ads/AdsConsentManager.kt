package br.alexandregpereira.hunter.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdsConsentManager(context: Context) {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    init {
        MobileAds.initialize(context)
    }

    private val _canRequestAds = MutableStateFlow(consentInformation.canRequestAds())
    val canRequestAds: StateFlow<Boolean> = _canRequestAds.asStateFlow()

    fun requestConsent(activity: Activity, debugHashedId: String? = null) {
        val debugSettings = debugHashedId?.let {
            ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId(it)
                .build()
        }
        val params = ConsentRequestParameters.Builder()
            .apply { debugSettings?.let { setConsentDebugSettings(it) } }
            .build()
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                    _canRequestAds.value = consentInformation.canRequestAds()
                }
            },
            {
                _canRequestAds.value = consentInformation.canRequestAds()
            },
        )
    }
}
