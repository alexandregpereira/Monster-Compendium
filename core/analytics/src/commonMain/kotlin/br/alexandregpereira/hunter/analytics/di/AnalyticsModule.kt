/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
