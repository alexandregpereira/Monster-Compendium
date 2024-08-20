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
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetAlphabetUseCaseTest {

    private val useCase = GetAlphabetUseCase()

    @Test
    fun invoke() = runTest {
        val items = listOf(
            MonsterCompendiumItem.Title(id = "Z0", value = "Z", isHeader = false),
            MonsterCompendiumItem.Title(id = "A0", value = "A", isHeader = true),
            MonsterCompendiumItem.Item(
                monster = Monster(
                    index = "arvore1",
                    name = "Arvore1",
                )
            ),
            MonsterCompendiumItem.Title(id = "E0", value = "E", isHeader = true),
            MonsterCompendiumItem.Title(id = "Elementals0", value = "Elementals", isHeader = false),
            MonsterCompendiumItem.Item(
                monster = Monster(
                    index = "air-elemental",
                    name = "Air Elemental",
                    group = "Elementals",
                )
            ),
            MonsterCompendiumItem.Title(id = "E1", value = "E", isHeader = false),
            MonsterCompendiumItem.Item(
                monster = Monster(
                    index = "elemental",
                    name = "Elemental",
                    imageData = MonsterImageData(isHorizontal = true)
                )
            ),
        )

        val result = useCase(items).single()
        assertEquals(
            actual = result,
            expected = listOf("A", "E", "Z")
        )
    }
}
