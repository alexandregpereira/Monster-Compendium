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
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Item
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType.BODY
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType.HEADER1
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType.HEADER2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetTableContentUseCaseTest {

    private val useCase = GetTableContentUseCase()

    @Test
    operator fun invoke() = runTest {
        val items = listOf(
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
                    imageData = MonsterImageData(isHorizontal = true)
                )
            ),
            Title(id = "E1", value = "E", isHeader = false),
            Item(
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
            expected = listOf(
                TableContentItem(text = "E", type = HEADER1, id = "E0"),
                TableContentItem(text = "Elementals", type = HEADER2, id = "Elementals0"),
                TableContentItem(text = "Air Elemental", type = BODY, id = "air-elemental"),
                TableContentItem(text = "Air Elemental2", type = BODY, id = "air-elemental2"),
                TableContentItem(text = "Air Elemental3", type = BODY, id = "air-elemental3"),
                TableContentItem(text = "E", type = HEADER2, id = "E1"),
                TableContentItem(text = "Elemental", type = BODY, id = "elemental")
            )
        )
    }
}
