/*
 * Copyright (c) 2024 Alexandre Gomes Pereira
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in this module directory
 * for the full license text.
 */

package br.alexandregpereira.hunter.analytics.di

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.analytics.JvmAmplitudeAnalytics
import com.amplitude.Amplitude
import org.koin.core.scope.Scope

internal actual fun Scope.createAmplitudeAnalytics(amplitudeApiKey: String): Analytics {
    val amplitude = Amplitude.getInstance()
    amplitude.init(amplitudeApiKey)
    return JvmAmplitudeAnalytics(
        amplitude = amplitude,
        jvmAnalyticsProvider = get(),
    )
}
