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

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.event.MonsterEvent

class MonsterDetailAnalytics(
    private val analytics: Analytics
) {

    private var loadTracked = false

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackMonsterDetailLoaded(monsterIndex: String, monsters: List<MonsterState>) {
        if (loadTracked) return
        analytics.track(
            eventName = "MonsterDetail - loaded",
            params = mapOf(
                "monsterIndex" to monsterIndex,
                "monstersSize" to monsters.size,
            )
        )
        loadTracked = true
    }

    fun trackMonsterDetailShown(event: MonsterEvent.OnVisibilityChanges.Show) {
        analytics.track(
            eventName = "MonsterDetail - monster detail shown",
            params = mapOf(
                "monsterIndex" to event.index,
                "monsterIndexes" to event.indexes.toString(),
                "enableMonsterPageChangesEventDispatch" to event.enableMonsterPageChangesEventDispatch,
            )
        )
    }

    fun trackMonsterDetailHidden() {
        analytics.track(
            eventName = "MonsterDetail - monster detail hidden",
        )
    }

    fun trackMonsterPageChanged(monsterIndex: String, scrolled: Boolean) {
        analytics.track(
            eventName = "MonsterDetail - page changed",
            params = mapOf(
                "monsterIndex" to monsterIndex,
                "scrolled" to scrolled,
            )
        )
    }

    fun trackMonsterDetailClosed() {
        analytics.track(
            eventName = "MonsterDetail - closed",
        )
    }

    fun trackMonsterDetailOptionsShown() {
        analytics.track(
            eventName = "MonsterDetail - options shown",
        )
    }

    fun trackMonsterDetailOptionsHidden() {
        analytics.track(
            eventName = "MonsterDetail - options hidden",
        )
    }

    fun trackMonsterDetailOptionClicked(option: MonsterDetailOptionState) {
        analytics.track(
            eventName = "MonsterDetail - option clicked",
            params = mapOf(
                "option" to option.name,
            )
        )
    }

    fun trackMonsterDetailSpellClicked(spellIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - spell clicked",
            params = mapOf(
                "spellIndex" to spellIndex,
            )
        )
    }

    fun trackMonsterDetailLoreClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - lore clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailDeleteClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - delete clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailEditClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - edit clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailDeleteConfirmed(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - delete confirmed",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailDeleteCanceled(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - delete canceled",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailResetToOriginalClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - reset to original clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailResetConfirmed(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - reset confirmed",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailExportClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - export clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }
}