/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.revenue

import br.alexandregpereira.hunter.analytics.Analytics
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

internal class IsPremiumImpl(
    private val revenueSdk: RevenueSdk,
    private val settings: Settings,
    private val analytics: Analytics,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : IsPremium {

    override suspend operator fun invoke(ignoreCache: Boolean): Boolean {
        val isPremiumFallbackCacheKey = "isPremiumFallbackCache"
        return try {
            revenueSdk.isPremiumEnabled(ignoreCache).also { isPremium ->
                val hasPremiumFallbackCache = withContext(dispatcher) {
                    settings.hasKey(isPremiumFallbackCacheKey)
                }
                if (isPremium && !hasPremiumFallbackCache) {
                    analytics.track(
                        eventName = "Revenue Session Ended - Premium User",
                        params = mapOf("sessionEndTime" to Clock.System.now().toEpochMilliseconds()),
                    )
                }
                settings.putBoolean(key = isPremiumFallbackCacheKey, value = isPremium)
            }
        } catch (cause: Throwable) {
            analytics.logException(cause)
            withContext(dispatcher) {
                settings.getBoolean(key = isPremiumFallbackCacheKey, defaultValue = true)
            }
        }
    }
}
