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

import br.alexandregpereira.hunter.domain.usecase.GetMonsterImagesUseCase
import br.alexandregpereira.hunter.domain.usecase.GetRemoteMonstersBySourceUseCase
import br.alexandregpereira.hunter.domain.usecase.appendMonsterImages
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class GetRemoteMonstersBySectionUseCase internal constructor(
    private val getRemoteMonstersBySourceUseCase: GetRemoteMonstersBySourceUseCase,
    private val getMonstersBySectionUseCase: GetMonstersBySectionUseCase,
    private val getMonsterImagesUseCase: GetMonsterImagesUseCase,
) {

    operator fun invoke(sourceAcronym: String): Flow<List<MonsterCompendiumItem>> {
        return getMonstersBySectionUseCase(
            flow {
                val monsters = coroutineScope {
                    val monstersDeferred = async {
                        getRemoteMonstersBySourceUseCase(sourceAcronym).single()
                    }

                    val imagesDeferred = async {
                        getMonsterImagesUseCase().single()
                    }

                    monstersDeferred.await().appendMonsterImages(imagesDeferred.await())
                }
                emit(monsters)
            }
        )
    }
}