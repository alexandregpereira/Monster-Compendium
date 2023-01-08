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

package br.alexandregpereira.hunter.domain.monster.lore

import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.single

class SyncMonstersLoreUseCase @Inject constructor(
    private val repository: MonsterLoreRepository,
    private val alternativeSourceRepository: MonsterLoreSourceRepository,
    private val settingsRepository: MonsterLoreSettingsRepository,
) {

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return alternativeSourceRepository.getMonsterLoreSources()
            .catch { emit(emptyList()) }
            .map { sources ->
                sources.takeIf { it.isNotEmpty() }
                    ?.asFlow()
                    ?.flatMapMerge { source ->
                        val lang = settingsRepository.getLanguage().single()
                        repository.getRemoteMonstersLore(source.acronym, lang = lang)
                            .catch { emit(emptyList()) }
                    }?.reduce { accumulator, value -> accumulator + value } ?: emptyList()
            }
            .flatMapLatest { monstersLore ->
                repository.save(monstersLore)
            }
    }
}
