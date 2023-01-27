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

import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.single

class GetMonsterImagesUseCase(
    private val repository: MonsterImageRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<MonsterImage>> {
        return repository.getMonsterImageJsonUrl().flatMapLatest { url ->
            repository.getMonsterImages(url).catch {
                emit(repository.getMonsterImages(DEFAULT_MONSTER_IMAGES_JSON_URL).single())
            }
        }.catch { emit(emptyList()) }
    }

    companion object {
        private const val DEFAULT_MONSTER_IMAGES_JSON_URL =
            "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/json/" +
                    "monster-images.json"
    }
}
