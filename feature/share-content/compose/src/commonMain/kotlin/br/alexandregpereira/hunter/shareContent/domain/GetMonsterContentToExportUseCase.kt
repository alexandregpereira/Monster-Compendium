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

package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun interface GetMonstersContentToExport {

    operator fun invoke(monsterIndexes: List<String>): Flow<ContentToExport>
}

internal class GetMonstersContentToExportUseCase(
    private val getMonsters: GetMonstersByIdsUseCase,
    private val getMonstersLore: GetMonstersLoreByIdsUseCase,
    private val getSpellsByIds: GetSpellsByIdsUseCase,
    private val getMonstersContentEditedToExport: GetMonstersContentEditedToExport,
): GetMonstersContentToExport {

    override fun invoke(monsterIndexes: List<String>): Flow<ContentToExport> {
        return if (monsterIndexes.isEmpty()) {
            getMonstersContentEditedToExport()
        } else {
            getMonsters(monsterIndexes).map { monsters ->
                val contentJson = monsters.getContentToExport(
                    getMonstersLore,
                    getSpellsByIds
                )
                ContentToExport(
                    contentJson = contentJson,
                    monsterImagePaths = monsters.getImagePaths(),
                )
            }
        }
    }
}

internal fun List<Monster>.getImagePaths(): List<String> {
    return filter {
        it.imageData.url.startsWith("file://")
    }.map {
        it.imageData.url
    }
}


internal data class ContentToExport(
    val contentJson: String,
    val monsterImagePaths: List<String>,
)
