/*
 * Copyright (c) 2024 Alexandre Gomes Pereira
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in this module directory
 * for the full license text.
 */

package br.alexandregpereira.hunter.analytics

import cocoapods.AmplitudeSwift.AMPIdentify
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

    override fun setUserProperty(name: String, value: Any) {
        val identify = AMPIdentify()
        identify.set(property = name, value = value)
        amplitude.identify(identify)
    }

    override fun getDeviceId(): String? {
        return amplitude.getDeviceId()
    }

    override fun logException(throwable: Throwable) {
        // no-op
    }
}

private fun Map<String, Any?>.toNSDictionary(): Map<Any?, *> =
    mapKeys { it.key as Any? }
