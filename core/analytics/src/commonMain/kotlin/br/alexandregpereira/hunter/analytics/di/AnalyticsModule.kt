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
import br.alexandregpereira.hunter.analytics.AnalyticsProviders
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun analyticsModule(amplitudeApiKey: String) = module {
    factory<Analytics> {
        AnalyticsProviders(
            firebaseAnalytics = get(qualifier(name = "FirebaseAnalytics")),
            amplitudeAnalytics = get(qualifier(name = "AmplitudeAnalytics"))
        )
    }
    single(qualifier = qualifier(name = "FirebaseAnalytics")) { createAnalytics() }
    single(qualifier = qualifier(name = "AmplitudeAnalytics")) {
        createAmplitudeAnalytics(amplitudeApiKey)
    }
}

internal expect fun Scope.createAnalytics(): Analytics
internal expect fun Scope.createAmplitudeAnalytics(amplitudeApiKey: String): Analytics
