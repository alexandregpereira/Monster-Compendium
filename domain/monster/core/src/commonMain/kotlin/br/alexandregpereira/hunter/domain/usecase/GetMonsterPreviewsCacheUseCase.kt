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
import br.alexandregpereira.hunter.domain.repository.MonsterCacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetMonsterPreviewsCacheUseCase internal constructor(
    private val getMonsterPreviewsUseCase: GetMonsterPreviewsUseCase,
    private val cacheRepository: MonsterCacheRepository
) {

    operator fun invoke(invalidateCache: Boolean = false): Flow<List<Monster>> {
        if (invalidateCache) {
            return getMonsterPreviewsUseCase()
        }
        return cacheRepository.getMonsters().map { monsters ->
            monsters.ifEmpty { getMonsterPreviewsUseCase().single() }
        }
    }
}
