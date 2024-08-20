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

package br.alexandregpereira.hunter.monster.content

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource

internal class MonsterContentManagerAnalytics(
    private val analytics: Analytics
) {

    fun trackMonsterContentLoaded(monsterContents: List<AlternativeSource>) {
        analytics.track(
            eventName = "MonsterContentManager - monster contents loaded",
            params = mapOf(
                "monsterContents" to monsterContents.map { it.originalName }.toString(),
            )
        )
    }

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackAddContentClick(acronym: String) {
        analytics.track(
            eventName = "MonsterContentManager - add content click",
            params = mapOf(
                "acronym" to acronym,
            )
        )
    }

    fun trackRemoveContentClick(acronym: String) {
        analytics.track(
            eventName = "MonsterContentManager - remove content click",
            params = mapOf(
                "acronym" to acronym,
            )
        )
    }

    fun trackPreviewContentClick(acronym: String, name: String) {
        analytics.track(
            eventName = "MonsterContentManager - preview click",
            params = mapOf(
                "acronym" to acronym,
                "contentName" to name,
            )
        )
    }

    fun trackClose() {
        analytics.track(eventName = "MonsterContentManager - close")
    }
}