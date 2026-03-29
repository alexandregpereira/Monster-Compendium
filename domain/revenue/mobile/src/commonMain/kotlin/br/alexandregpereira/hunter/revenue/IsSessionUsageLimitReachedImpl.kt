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

internal class IsSessionUsageLimitReachedImpl internal constructor(
    private val isPremium: IsPremium,
    private val revenueSessionRemoteConfig: RevenueSessionRemoteConfig,
    private val revenueSessionTimeDataSource: RevenueSessionTimeDataSource,
    private val analytics: Analytics,
) : IsSessionUsageLimitReached {

    override suspend operator fun invoke(): Boolean {
        if (isPremium()) {
            return false
        }

        val sessionTimeLimitInMillis = try {
            revenueSessionRemoteConfig.getSessionTimeLimitInMillis()
        } catch (cause: Throwable) {
            analytics.logException(cause)
            return false
        }

        val sessionTime = revenueSessionTimeDataSource.getSessionTime()
        val isSessionUsageLimitReached = sessionTime >= sessionTimeLimitInMillis
        return isSessionUsageLimitReached
    }
}
