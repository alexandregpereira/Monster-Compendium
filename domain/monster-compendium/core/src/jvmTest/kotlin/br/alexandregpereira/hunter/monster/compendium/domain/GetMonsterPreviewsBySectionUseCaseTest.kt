/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
