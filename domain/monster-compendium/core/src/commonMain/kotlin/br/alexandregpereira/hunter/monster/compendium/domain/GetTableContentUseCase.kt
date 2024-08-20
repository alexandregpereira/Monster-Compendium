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
