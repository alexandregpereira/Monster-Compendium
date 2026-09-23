/*
 * Copyright (c) 2024 Alexandre Gomes Pereira
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in this module directory
 * for the full license text.
 */

package br.alexandregpereira.hunter.analytics

import com.amplitude.android.Amplitude
import com.amplitude.android.events.Identify

internal class AndroidAmplitudeAnalytics(
    private val amplitude: Amplitude,
) : Analytics {

    override fun track(
        eventName: String,
        params: Map<String, Any?>
    ) {
        amplitude.track(eventName, params)
    }

    override fun setUserProperty(name: String, value: Any) {
        amplitude.identify(Identify().set(name, value))
    }

    override fun getDeviceId(): String? {
        return amplitude.getDeviceId()
    }

    override fun logException(throwable: Throwable) {
        // no-op
    }
}
