package br.alexandregpereira.hunter.analytics

import com.amplitude.Amplitude
import com.amplitude.Event
import org.json.JSONObject

internal class JvmAmplitudeAnalytics(
    private val amplitude: Amplitude,
    private val jvmAnalyticsProvider: JvmAnalyticsProvider,
) : Analytics {

    override fun track(
        eventName: String,
        params: Map<String, Any?>
    ) {
        val userId = null
        val deviceId = getDeviceId()
        val event = Event(eventName, userId, deviceId)
        event.appVersion = jvmAnalyticsProvider.getVersionName()
        event.platform = "Desktop"
        event.osName = System.getProperty("os.name") ?: "unknown"
        event.osVersion = System.getProperty("os.version") ?: "unknown"
        event.deviceModel = System.getProperty("os.arch") ?: "unknown"
        val jsonParams = JSONObject()
        for ((key, value) in params) {
            jsonParams.put(key, value)
        }
        event.eventProperties = jsonParams
        amplitude.logEvent(event)
    }

    override fun logException(throwable: Throwable) {
        // No-op
    }

    private fun getDeviceId(): String {
        // Generate a unique device id using uuid checking if it was already generated and stored in a file
        return ""
    }
}