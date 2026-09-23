/*
 * Copyright (c) 2024 Alexandre Gomes Pereira
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in this module directory
 * for the full license text.
 */

package br.alexandregpereira.hunter.analytics.di

import android.content.Context
import android.content.pm.ApplicationInfo
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.analytics.FirebaseAnalytics
import org.koin.core.scope.Scope

internal actual fun Scope.createAnalytics(): Analytics {
    val context: Context = get()
    val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    return FirebaseAnalytics(
        analytics = get(),
        crashlytics = get(),
        isDebug = isDebug,
    )
}
