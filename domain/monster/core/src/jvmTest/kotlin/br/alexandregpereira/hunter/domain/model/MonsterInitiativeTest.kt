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

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MonsterInitiativeTest {

    @Test
    fun `initiativeWithFallback returns stats initiative when it is set`() {
        val monster = MonsterFactory.createEmpty("goblin").copy(
            stats = Stats(armorClass = 15, hitPoints = 7, hitDice = "2d6", initiative = 3),
            abilityScores = listOf(
                AbilityScore(type = AbilityScoreType.DEXTERITY, value = 14, modifier = 2)
            )
        )

        assertEquals(3, monster.initiativeWithFallback)
    }

    @Test
    fun `initiativeWithFallback returns DEX modifier when stats initiative is null`() {
        val monster = MonsterFactory.createEmpty("goblin").copy(
            stats = Stats(armorClass = 15, hitPoints = 7, hitDice = "2d6", initiative = null),
            abilityScores = listOf(
                AbilityScore(type = AbilityScoreType.STRENGTH, value = 10, modifier = 0),
                AbilityScore(type = AbilityScoreType.DEXTERITY, value = 14, modifier = 2),
            )
        )

        assertEquals(2, monster.initiativeWithFallback)
    }

    @Test
    fun `initiativeWithFallback returns null when stats initiative is null and no DEX score exists`() {
        val monster = MonsterFactory.createEmpty("goblin").copy(
            stats = Stats(armorClass = 15, hitPoints = 7, hitDice = "2d6", initiative = null),
            abilityScores = listOf(
                AbilityScore(type = AbilityScoreType.STRENGTH, value = 10, modifier = 0),
            )
        )

        assertNull(monster.initiativeWithFallback)
    }

    @Test
    fun `initiativeWithFallback returns null when stats initiative is null and ability scores are empty`() {
        val monster = MonsterFactory.createEmpty("goblin").copy(
            stats = Stats(armorClass = 15, hitPoints = 7, hitDice = "2d6", initiative = null),
            abilityScores = emptyList()
        )

        assertNull(monster.initiativeWithFallback)
    }

    @Test
    fun `initiativeWithFallback prefers stats initiative over DEX modifier when both present`() {
        val monster = MonsterFactory.createEmpty("goblin").copy(
            stats = Stats(armorClass = 15, hitPoints = 7, hitDice = "2d6", initiative = -1),
            abilityScores = listOf(
                AbilityScore(type = AbilityScoreType.DEXTERITY, value = 20, modifier = 5)
            )
        )

        assertEquals(-1, monster.initiativeWithFallback)
    }
}
