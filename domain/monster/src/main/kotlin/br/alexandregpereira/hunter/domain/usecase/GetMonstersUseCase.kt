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

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.sort.sortMonstersByNameAndGroup
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.single

class GetMonstersUseCase @Inject internal constructor(
    private val getMonsterPreviewsUseCase: GetMonsterPreviewsUseCase,
    private val getMonstersByIdsUseCase: GetMonstersByIdsUseCase
) {

    private val monsterPagerScrollLimit = 100

    operator fun invoke(monsterIndex: String): Flow<List<Monster>> {
        return getMonsterPreviewsUseCase()
            .sortMonstersByNameAndGroup()
            .mapNotNull { monstersPreview ->
                val monsterIndexes = monstersPreview.map { it.index }

                monsterIndexes.indexOf(monsterIndex)
                    .takeIf { it >= 0 }
                    ?.let { position ->
                        val fromIndex = position - monsterPagerScrollLimit
                        val toIndex = position + monsterPagerScrollLimit
                        monsterIndexes.subList(
                            fromIndex.coerceAtLeast(0),
                            toIndex.coerceAtMost(monstersPreview.lastIndex)
                        )
                    }?.let { monsterIndexesSubList ->
                        getMonstersByIdsUseCase(monsterIndexesSubList).single()
                    }
            }
    }
}
