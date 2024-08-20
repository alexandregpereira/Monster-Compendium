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

package br.alexandregpereira.hunter.monster.lore.detail

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore

internal class MonsterLoreDetailAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackMonsterLoreDetailLoaded(monsterLore: MonsterLore) {
        analytics.track(
            eventName = "MonsterLoreDetail - loaded",
            params = mapOf(
                "loreIndex" to monsterLore.index,
                "loreName" to monsterLore.name,
                "firstEntryTitle" to monsterLore.entries.firstOrNull()?.title.orEmpty(),
                "firstEntryDescription" to monsterLore.entries.firstOrNull()?.description.orEmpty(),
            )
        )
    }

    fun trackMonsterLoreDetailOpened(index: String) {
        analytics.track(
            eventName = "MonsterLoreDetail - opened",
            params = mapOf(
                "loreIndex" to index,
            )
        )
    }

    fun trackMonsterLoreDetailClosed() {
        analytics.track(
            eventName = "MonsterLoreDetail - closed",
        )
    }
}