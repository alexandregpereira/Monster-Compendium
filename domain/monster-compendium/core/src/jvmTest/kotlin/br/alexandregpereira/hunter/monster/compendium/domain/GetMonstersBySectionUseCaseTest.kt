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

package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.CompendiumSortType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Item
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetMonstersBySectionUseCaseTest {

    private val useCase = GetMonstersBySectionUseCase()

    private val goblin = createMonster(index = "goblin", name = "Goblin", challengeRating = 0.25f)
    private val ancientDragon = createMonster(index = "ancient-dragon", name = "Ancient Dragon", challengeRating = 20f)
    private val bandit = createMonster(index = "bandit", name = "Bandit", challengeRating = 0.125f)
    private val ogre = createMonster(index = "ogre", name = "Ogre", challengeRating = 2f)
    private val boar = createMonster(index = "boar", name = "Boar", challengeRating = 0.25f)

    @Test
    fun `invoke When sort type is challenge rating desc Should create sections from the highest challenge rating`() = runTest {
        val result = useCase(
            monstersFlow = flowOf(listOf(goblin, ancientDragon, bandit, ogre, boar)),
            sortType = CompendiumSortType.CHALLENGE_RATING_DESC,
        ).single()

        assertEquals(
            expected = listOf(
                Title(id = "cr-20.0", value = "20", isHeader = true),
                Item(monster = ancientDragon),
                Title(id = "cr-2.0", value = "2", isHeader = true),
                Item(monster = ogre),
                Title(id = "cr-0.25", value = "1/4", isHeader = true),
                Item(monster = boar),
                Item(monster = goblin),
                Title(id = "cr-0.125", value = "1/8", isHeader = true),
                Item(monster = bandit),
            ),
            actual = result,
        )
    }

    @Test
    fun `invoke When sort type is challenge rating asc Should create a section per challenge rating`() = runTest {
        val result = useCase(
            monstersFlow = flowOf(listOf(goblin, ancientDragon, bandit, ogre, boar)),
            sortType = CompendiumSortType.CHALLENGE_RATING_ASC,
        ).single()

        assertEquals(
            expected = listOf(
                Title(id = "cr-0.125", value = "1/8", isHeader = true),
                Item(monster = bandit),
                Title(id = "cr-0.25", value = "1/4", isHeader = true),
                Item(monster = boar),
                Item(monster = goblin),
                Title(id = "cr-2.0", value = "2", isHeader = true),
                Item(monster = ogre),
                Title(id = "cr-20.0", value = "20", isHeader = true),
                Item(monster = ancientDragon),
            ),
            actual = result,
        )
    }

    @Test
    fun `alphabet When sort type is challenge rating Should keep the numeric order`() = runTest {
        val items = useCase(
            monstersFlow = flowOf(
                listOf(
                    createMonster(index = "a", name = "A", challengeRating = 10f),
                    createMonster(index = "b", name = "B", challengeRating = 2f),
                    createMonster(index = "c", name = "C", challengeRating = 0.5f),
                )
            ),
            sortType = CompendiumSortType.CHALLENGE_RATING_ASC,
        ).single()

        val result = GetAlphabetUseCase()(items, CompendiumSortType.CHALLENGE_RATING_ASC).single()

        assertEquals(expected = listOf("1/2", "2", "10"), actual = result)
    }

    private fun createMonster(
        index: String,
        name: String,
        challengeRating: Float,
    ): Monster = MonsterFactory.createEmpty(index = index).copy(
        name = name,
        challengeRatingData = ChallengeRating.create(challengeRating),
    )
}
