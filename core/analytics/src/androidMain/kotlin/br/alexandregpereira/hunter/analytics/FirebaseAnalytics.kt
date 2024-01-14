package br.alexandregpereira.hunter.analytics

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal class FirebaseAnalytics(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) : Analytics {

    override fun track(eventName: String, params: Map<String, Any?>) {
        val eventNameNormalized = eventName.replace(" ", "_")
            .replace("-", "")

        val bundle = Bundle()
        params.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Long -> bundle.putLong(key, value)
                is Float -> bundle.putFloat(key, value)
                is Double -> bundle.putDouble(key, value)
            }
        }
        analytics.logEvent(eventNameNormalized, bundle)
        if (BuildConfig.DEBUG) {
            Log.d("FirebaseAnalytics", "event name: $eventNameNormalized. params: $params")
        }
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(RuntimeException(throwable))
    }
}