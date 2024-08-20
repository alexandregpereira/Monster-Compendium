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

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Item
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class GetMonsterPreviewsBySectionUseCaseTest {

    private val getMonstersUseCase: GetMonsterPreviewsUseCase = mockk()
    private val useCase = GetMonsterPreviewsBySectionUseCase(
        getMonstersUseCase,
        GetMonstersBySectionUseCase(),
    )

    @Test
    fun `invoke When first monster has group is a group Should return title in sequence`() = runTest {
        val monster = listOf(
            Monster(
                index = "air-elemental",
                name = "Air Elemental",
                group = "Elementals",
            ),
            Monster(
                index = "air-elemental2",
                name = "Air Elemental2",
                group = "Elementals",
            ),
            Monster(
                index = "air-elemental3",
                name = "Air Elemental3",
                group = "Elementals",
            ),
            Monster(
                index = "elemental",
                name = "Elemental",
            ),
        )

        every { getMonstersUseCase() } returns flowOf(monster)

        val result = useCase().single()

        assertEquals(
            actual = result,
            expected = listOf(
                Title(id = "E0", value = "E", isHeader = true),
                Title(id = "Elementals0", value = "Elementals", isHeader = false),
                Item(
                    monster = Monster(
                        index = "air-elemental",
                        name = "Air Elemental",
                        group = "Elementals",
                    )
                ),
                Item(
                    monster = Monster(
                        index = "air-elemental2",
                        name = "Air Elemental2",
                        group = "Elementals",
                    )
                ),
                Item(
                    monster = Monster(
                        index = "air-elemental3",
                        name = "Air Elemental3",
                        group = "Elementals",
                    )
                ),
                Title(id = "E1", value = "E", isHeader = false),
                Item(
                    monster = Monster(
                        index = "elemental",
                        name = "Elemental",
                    )
                ),
            )
        )
    }

    @Test
    fun `invoke When first monster has no group Should return no title in sequence`() = runTest {
        val monster = listOf(
            Monster(
                index = "air-elemental",
                name = "Air Elemental",
            ),
            Monster(
                index = "air-elemental2",
                name = "Air Elemental2",
            ),
            Monster(
                index = "air-elemental3",
                name = "Air Elemental3",
            ),
            Monster(
                index = "elemental",
                name = "Elemental",
            ),
        )

        every { getMonstersUseCase() } returns flowOf(monster)

        val result = useCase().single()

        assertEquals(
            actual = result,
            expected = listOf(
                Title(id = "A0", value = "A", isHeader = true),
                Item(
                    monster = Monster(
                        index = "air-elemental",
                        name = "Air Elemental",
                    )
                ),
                Item(
                    monster = Monster(
                        index = "air-elemental2",
                        name = "Air Elemental2",
                    )
                ),
                Item(
                    monster = Monster(
                        index = "air-elemental3",
                        name = "Air Elemental3",
                    )
                ),
                Title(id = "E0", value = "E", isHeader = true),
                Item(
                    monster = Monster(
                        index = "elemental",
                        name = "Elemental",
                    )
                ),
            )
        )
    }
}
