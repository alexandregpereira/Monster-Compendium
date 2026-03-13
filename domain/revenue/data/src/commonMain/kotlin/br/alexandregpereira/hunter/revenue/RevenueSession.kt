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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.Clock

class RevenueSession internal constructor(
    private val revenueSessionTimeDataSource: RevenueSessionTimeDataSource,
    private val analytics: Analytics,
    private val isPremium: IsPremium,
    private val revenueSdk: RevenueSdk,
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var startTime: Long? = null

    fun start() {
        revenueSdk.initialize()
        coroutineScope.launch {
            val time = Clock.System.now().toEpochMilliseconds()
            if (isPremium()) return@launch

            startTime = time
            analytics.track(
                eventName = "RevenueSession - started",
                params = mapOf("sessionStartTime" to time),
            )
        }
    }

    fun stop() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val startTime = startTime
        val totalSessionTime = if (startTime != null) {
            currentTime - startTime
        } else {
            0L
        }
        coroutineScope.launch {
            revenueSessionTimeDataSource.setSessionTime(totalSessionTime)
            analytics.track(
                eventName = "RevenueSession - stopped",
                params = mapOf("sessionTime" to totalSessionTime),
            )
        }
    }
}
