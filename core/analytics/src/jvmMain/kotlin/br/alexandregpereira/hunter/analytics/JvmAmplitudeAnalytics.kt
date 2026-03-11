package br.alexandregpereira.hunter.analytics

import com.amplitude.Amplitude
import com.amplitude.Event
import org.json.JSONObject
import java.io.File
import java.util.Locale
import java.util.UUID

internal class JvmAmplitudeAnalytics(
    private val amplitude: Amplitude,
    private val jvmAnalyticsProvider: JvmAnalyticsProvider,
) : Analytics {

    private val deviceId: String by lazy { getDeviceIdFromFile() }
    private val sessionId: Long by lazy { System.currentTimeMillis() }

    override fun track(
        eventName: String,
        params: Map<String, Any?>
    ) {
        val userId = null
        val event = Event(eventName, userId, deviceId)
        event.appVersion = jvmAnalyticsProvider.getVersionName()
        event.platform = "Desktop"
        event.osName = System.getProperty("os.name") ?: "unknown"
        event.osVersion = System.getProperty("os.version") ?: "unknown"
        event.deviceModel = System.getProperty("os.arch") ?: "unknown"
        event.country = Locale.getDefault().displayCountry
        event.language = Locale.getDefault().language
        event.ip = $$"$remote"
        event.sessionId = sessionId
        val jsonParams = JSONObject()
        for ((key, value) in params) {
            jsonParams.put(key, value)
        }
        event.eventProperties = jsonParams
        println("Tracking event: $event")
        amplitude.logEvent(event)
    }

    override fun logException(throwable: Throwable) {
        // No-op
    }

    private fun getDeviceIdFromFile(): String {
        val deviceIdFile = File(
            System.getProperty("user.home"),
            ".monster-compendium/device_id"
        )
        return try {
            // Check if device ID already exists
            if (deviceIdFile.exists()) {
                deviceIdFile.readText().trim()
            } else {
                // Generate new device ID
                val newDeviceId = UUID.randomUUID().toString()

                // Create parent directories if they don't exist
                deviceIdFile.parentFile?.mkdirs()

                // Save the device ID
                deviceIdFile.writeText(newDeviceId)

                newDeviceId
            }
        } catch (@Suppress("UNUSED_PARAMETER") e: Exception) {
            // Fallback to a temporary UUID if file operations fail
            UUID.randomUUID().toString()
        }
    }
}
