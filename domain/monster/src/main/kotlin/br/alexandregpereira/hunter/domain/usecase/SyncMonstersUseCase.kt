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

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterSource
import br.alexandregpereira.hunter.domain.repository.MonsterAlternativeSourceRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
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
import kotlinx.coroutines.flow.zip

@OptIn(FlowPreview::class)
class SyncMonstersUseCase @Inject internal constructor(
    private val repository: MonsterRepository,
    private val alternativeSourceRepository: MonsterAlternativeSourceRepository,
    private val monsterSettingsRepository: MonsterSettingsRepository,
    private val getMonsterImages: GetMonsterImagesUseCase,
    private val saveMonstersUseCase: SaveMonstersUseCase
) {

    private val srdSource = MonsterSource("SRD", "SRD")

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return alternativeSourceRepository.getAlternativeSources()
            .catch { emit(emptyList()) }
            .zip(getMonsterImages()) { alternativeSources, monsterImages ->
                alternativeSources.map { it.source }
                    .run { this + srdSource }
                    .asFlow()
                    .flatMapMerge { source ->
                        source.getRemoteMonsters(monsterImages)
                    }
                    .reduce { accumulator, value -> accumulator + value }
            }
            .flatMapLatest {
                saveMonstersUseCase(monsters = it, isSync = true)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun MonsterSource.getRemoteMonsters(monsterImages: List<MonsterImage>): Flow<List<Monster>> {
        return if (this == srdSource) {
            monsterSettingsRepository.getLanguage().flatMapLatest {
                repository.getRemoteMonsters(lang = it)
            }
        } else {
            monsterSettingsRepository.getLanguage().flatMapLatest {
                repository.getRemoteMonsters(
                    sourceAcronym = this.acronym,
                    lang = it
                ).catch {
                    emit(emptyList())
                }
            }
        }.appendMonsterImages(monsterImages)
    }

    private fun Flow<List<Monster>>.appendMonsterImages(
        monsterImages: List<MonsterImage>
    ): Flow<List<Monster>> = map {
        it.map { monster ->
            val monsterImage = monsterImages.firstOrNull { monsterImage ->
                monsterImage.monsterIndex == monster.index
            } ?: MonsterImage(
                monsterIndex = monster.index,
                backgroundColor = Color(light = "#e0dfd1", dark = "#e0dfd1"),
                isHorizontalImage = false,
                imageUrl = DEFAULT_IMAGE_BASE_URL + "default-${monster.type.name.lowercase()}.png"
            )

            monster.copy(
                preview = monster.preview.copy(
                    imageData = MonsterImageData(
                        url = monsterImage.imageUrl,
                        backgroundColor = monsterImage.backgroundColor,
                        isHorizontal = monsterImage.isHorizontalImage
                    )
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_IMAGE_BASE_URL =
            "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/"
    }
}
