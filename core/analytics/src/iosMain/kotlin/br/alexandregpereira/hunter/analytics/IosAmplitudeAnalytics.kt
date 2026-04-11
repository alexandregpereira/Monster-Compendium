package br.alexandregpereira.hunter.analytics

import cocoapods.AmplitudeSwift.Amplitude

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
internal class IosAmplitudeAnalytics(
    private val amplitude: Amplitude,
) : Analytics {

    override fun track(eventName: String, params: Map<String, Any?>) {
        println("Amplitude: event name: $eventName. params: $params")
        amplitude.track(
            eventType = eventName,
            eventProperties = params.toNSDictionary()
        )
    }

    override fun logException(throwable: Throwable) {
        // no-op
    }
}

private fun Map<String, Any?>.toNSDictionary(): Map<Any?, *> =
    mapKeys { it.key as Any? }
