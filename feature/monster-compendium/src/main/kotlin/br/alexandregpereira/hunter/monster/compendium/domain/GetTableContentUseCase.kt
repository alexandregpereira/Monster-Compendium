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

import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType.HEADER1
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType.HEADER2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetTableContentUseCase internal constructor() {

    operator fun invoke(items: List<MonsterCompendiumItem>): Flow<List<TableContentItem>> {
        return flowOf(items).map {
            it.map { item ->
                when (item) {
                    is MonsterCompendiumItem.Title -> {
                        val type = if (item.isHeader) HEADER1 else HEADER2
                        TableContentItem(text = item.value, type = type, id = item.id)
                    }
                    is MonsterCompendiumItem.Item -> TableContentItem(
                        text = item.monster.name,
                        type = TableContentItemType.BODY,
                        id = item.monster.index
                    )
                }
            }
        }
    }
}
