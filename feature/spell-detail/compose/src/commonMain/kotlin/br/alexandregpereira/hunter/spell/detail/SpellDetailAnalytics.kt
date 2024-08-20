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

package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.analytics.Analytics

internal class SpellDetailAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackSpellLoaded(spell: SpellState) {
        analytics.track(
            eventName = "Spell Detail - loaded",
            params = mapOf(
                "index" to spell.index,
                "name" to spell.name
            )
        )
    }

    fun trackSpellShown(index: String) {
        analytics.track(
            eventName = "Spell Detail - shown",
            params = mapOf(
                "index" to index
            )
        )
    }

    fun trackSpellClosed() {
        analytics.track(
            eventName = "Spell Detail - closed",
        )
    }
}