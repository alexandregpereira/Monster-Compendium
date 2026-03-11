package br.alexandregpereira.hunter.analytics

import com.amplitude.android.Amplitude

internal class AndroidAmplitudeAnalytics(
    private val amplitude: Amplitude,
) : Analytics {

    override fun track(
        eventName: String,
        params: Map<String, Any?>
    ) {
        if (BuildConfig.DEBUG.not()) {
            amplitude.track(eventName, params)
        }
    }

    override fun logException(throwable: Throwable) {
        // no-op
    }
}
