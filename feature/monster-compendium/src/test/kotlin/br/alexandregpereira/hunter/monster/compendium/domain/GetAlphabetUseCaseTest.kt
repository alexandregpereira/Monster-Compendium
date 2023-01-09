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
