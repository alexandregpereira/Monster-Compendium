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
import br.alexandregpereira.hunter.analytics.EmptyAnalytics
import br.alexandregpereira.hunter.analytics.IosAmplitudeAnalytics
import cocoapods.AmplitudeSwift.AMPConfiguration
import cocoapods.AmplitudeSwift.Amplitude
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
internal actual fun Scope.createAmplitudeAnalytics(amplitudeApiKey: String): Analytics {
    return try {
        val config = AMPConfiguration(apiKey = amplitudeApiKey)
        val amplitude = Amplitude(configuration = config)
        IosAmplitudeAnalytics(amplitude)
    } catch (e: Throwable) {
        val firebaseAnalytics = get<Analytics>(qualifier(name = "FirebaseAnalytics"))
        firebaseAnalytics.logException(e)
        EmptyAnalytics()
    }
}
