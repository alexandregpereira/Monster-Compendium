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

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class RevenueSessionTimeDataSource(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val sessionTimeKey = "revenueSessionTime"

    suspend fun setSessionTime(time: Long) = withContext(dispatcher) {
        settings.putLong(sessionTimeKey, time)
    }

    suspend fun getSessionTime(): Long = withContext(dispatcher) {
        settings.getLong(sessionTimeKey, defaultValue = 0L)
    }

    suspend fun clearSessionTime() = withContext(dispatcher) {
        if (settings.hasKey(sessionTimeKey)) {
            settings.remove(sessionTimeKey)
        }
    }
}
