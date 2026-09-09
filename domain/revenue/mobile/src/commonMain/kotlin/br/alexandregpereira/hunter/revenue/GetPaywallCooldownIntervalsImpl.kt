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

internal class GetPaywallCooldownIntervalsImpl internal constructor(
    private val revenueRemoteConfig: RevenueRemoteConfig,
    private val analytics: Analytics,
) : GetPaywallCooldownIntervals {

    /**
     * Every failure falls back to [DEFAULT_PAYWALL_COOLDOWN_INTERVALS]. Degrading to an empty list
     * would mean no cooldown at all, which is the behavior this feature exists to prevent.
     */
    override suspend fun invoke(): List<Long> {
        return try {
            revenueRemoteConfig.getPaywallCooldownIntervalsInMillis()
                .takeIf { it.isNotEmpty() }
                ?: DEFAULT_PAYWALL_COOLDOWN_INTERVALS
        } catch (cause: RevenueRemoteConfigException) {
            if (cause !is RevenueRemoteConfigException.FailToFetchConfig) {
                analytics.logException(cause)
            }
            DEFAULT_PAYWALL_COOLDOWN_INTERVALS
        }
    }
}

internal val DEFAULT_PAYWALL_COOLDOWN_INTERVALS: List<Long> = listOf(
    3L * 24 * 60 * 60 * 1000,
    7L * 24 * 60 * 60 * 1000,
    14L * 24 * 60 * 60 * 1000,
    30L * 24 * 60 * 60 * 1000,
)
