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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

internal class RevenueSessionImpl(
    private val revenueSessionTimeDataSource: RevenueSessionTimeDataSource,
    private val analytics: Analytics,
    private val isPremium: IsPremium,
    private val revenueSdk: RevenueSdk,
    private val getCurrentOffer: GetCurrentOffer,
) : RevenueSession {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var startTime: Long? = null
    private val mutex = Mutex()

    override fun initialize(apiKey: String) {
        revenueSdk.initialize(apiKey)
    }

    override fun start() {
        coroutineScope.launch {
            if (isPremium()) return@launch

            val time = Clock.System.now().toEpochMilliseconds()
            mutex.withLock { startTime = time }
            analytics.track(
                eventName = "RevenueSession - started",
                params = mapOf("sessionStartTime" to time),
            )
            preloadCurrentOffer()
        }
    }

    /**
     * Warms the offer cache so the paywall does not wait on the offerings request to show a price.
     * Failures are ignored on purpose: ShouldShowPaywall and the paywall itself already report the
     * ones that matter, and logging here would repeat that signal on every foreground.
     */
    private suspend fun preloadCurrentOffer() {
        try {
            getCurrentOffer()
        } catch (cause: CancellationException) {
            throw cause
        } catch (_: Throwable) {
            // The paywall reports offer failures on the path where they affect the user.
        }
    }

    override fun stop() {
        val startTime = startTime ?: return
        val currentTime = Clock.System.now().toEpochMilliseconds()
        this.startTime = null
        coroutineScope.launch {
            val currentTotalSessionTime = revenueSessionTimeDataSource.getSessionTime()
            val totalSessionTime = (currentTime - startTime) + currentTotalSessionTime
            revenueSessionTimeDataSource.setSessionTime(totalSessionTime)
            analytics.track(
                eventName = "RevenueSession - stopped",
                params = mapOf("sessionTime" to totalSessionTime),
            )
        }
    }
}
