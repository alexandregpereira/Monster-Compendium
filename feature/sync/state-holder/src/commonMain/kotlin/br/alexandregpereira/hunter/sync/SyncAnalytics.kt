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

package br.alexandregpereira.hunter.sync

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.sync.model.SyncStatus

internal class SyncAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackStartSync() {
        analytics.track(
            eventName = "Sync - start",
        )
    }

    fun trackFinishSync() {
        analytics.track(
            eventName = "Sync - finish",
        )
    }

    fun trackSyncStatus(status: SyncStatus, forceSync: Boolean) {
        analytics.track(
            eventName = "Sync - synced",
            params = mapOf(
                "status" to status.name,
                "forceSync" to forceSync
            )
        )
    }

    fun trackTryAgain() {
        analytics.track(
            eventName = "Sync - try again",
        )
    }
}