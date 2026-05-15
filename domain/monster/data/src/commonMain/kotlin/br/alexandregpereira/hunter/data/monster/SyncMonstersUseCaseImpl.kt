/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesAdded
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.usecase.GetMonsterImagesUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.appendMonsterImages
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.single

internal class SyncMonstersUseCaseImpl(
    private val remoteRepository: MonsterRemoteRepository,
    private val localRepository: MonsterLocalRepository,
    private val getAlternativeSourcesAdded: GetAlternativeSourcesAdded,
    private val monsterSettingsRepository: MonsterSettingsRepository,
    private val getMonsterImages: GetMonsterImagesUseCase,
    private val saveMonstersUseCase: SaveMonstersUseCase,
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase
) : SyncMonstersUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(): Flow<Unit> {
        return flow {
            coroutineScope {
                val sources = async { getAlternativeSourcesAdded() }
                val monsterImages = async { getMonsterImages().single() }
                emit(sources.await() to monsterImages.await())
            }
        }.map { (alternativeSources, monsterImages) ->
                alternativeSources
                    .asFlow()
                    .flatMapMerge { source ->
                        getRemoteMonsters(source, monsterImages)
                    }
                    .reduce { accumulator, value -> accumulator + value }
            }
            .removeModifiedMonsters()
            .flatMapLatest { monsters ->
                saveMonstersUseCase(monsters = monsters, isSync = true)
            }.flatMapLatest {
                saveCompendiumScrollItemPositionUseCase(position = 0)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getRemoteMonsters(
        source: AlternativeSource,
        monsterImages: List<MonsterImage>,
    ): Flow<List<Monster>> {
        if (source.totalMonsters <= 0) return flowOf(emptyList())
        return monsterSettingsRepository.getLanguage().flatMapLatest {
            remoteRepository.getMonsters(
                sourceAcronym = source.acronym,
                lang = it
            ).catch { error ->
                error.printStackTrace()
                emit(emptyList())
            }
        }.appendMonsterImages(monsterImages)
    }

    private fun Flow<List<Monster>>.appendMonsterImages(
        monsterImages: List<MonsterImage>
    ): Flow<List<Monster>> = map {
        it.appendMonsterImages(monsterImages)
    }

    private fun Flow<List<Monster>>.removeModifiedMonsters(): Flow<List<Monster>> = map { monsters ->
        val monstersEditedIndexes = localRepository.getMonsterPreviewsEdited().single()
            .map { it.index }
            .toSet()
        monsters.filterNot { monstersEditedIndexes.contains(it.index) }
    }
}
