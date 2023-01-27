/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.search.domain

import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class SearchMonstersByNameUseCase internal constructor(
    private val monsterRepository: MonsterRepository
) {

    operator fun invoke(name: String): Flow<List<SearchMonsterResult>> {
        if (name.isBlank()) return flowOf(emptyList())

        return monsterRepository.getLocalMonstersByQuery("name LIKE '%$name%' ORDER BY name")
            .catch { error ->
                throw SearchMonstersByNameUnexpectedException(cause = error)
            }
            .map { monsters ->
                monsters.mapIndexed { index, monster ->
                    SearchMonsterResult(
                        index = monster.index,
                        name = monster.name,
                        type = monster.type,
                        challengeRating = monster.challengeRating,
                        imageUrl = monster.imageData.url,
                        backgroundColorLight = monster.imageData.backgroundColor.light,
                        backgroundColorDark = monster.imageData.backgroundColor.dark,
                        isHorizontalImage = index == 0 && monsters.size % 2 != 0
                    )
                }
            }
    }
}
