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

package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.Monster

internal fun Analytics.trackMonsterRegistrationOpened(monsterIndex: String) {
    track(
        eventName = "MonsterRegistration - opened",
        params = mapOf(
            "monsterIndex" to monsterIndex,
        ),
    )
}

internal fun Analytics.trackMonsterRegistrationClosed(monsterIndex: String) {
    track(
        eventName = "MonsterRegistration - closed",
        params = mapOf(
            "monsterIndex" to monsterIndex,
        ),
    )
}

internal fun Analytics.trackMonsterRegistrationSaved(monster: Monster) {
    track(
        eventName = "MonsterRegistration - saved",
        params = mapOf(
            "monsterIndex" to monster.index,
            "monster" to monster.toString(),
        ),
    )
}

internal fun Analytics.trackMonsterRegistrationTableContentClicked(key: String) {
    track(
        eventName = "MonsterRegistration - table content clicked",
        params = mapOf(
            "key" to key,
        ),
    )
}

internal fun Analytics.onTableContentClosed() {
    track(
        eventName = "MonsterRegistration - table content closed",
    )
}

internal fun Analytics.onTableContentOpened() {
    track(
        eventName = "MonsterRegistration - table content opened",
    )
}

internal fun Analytics.trackMonsterRegistrationSpellClicked(spellIndex: String) {
    track(
        eventName = "MonsterRegistration - spell clicked",
        params = mapOf(
            "spellIndex" to spellIndex,
        ),
    )
}
