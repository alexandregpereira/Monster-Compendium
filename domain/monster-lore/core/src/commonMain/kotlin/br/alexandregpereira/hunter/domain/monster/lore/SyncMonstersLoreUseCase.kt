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

package br.alexandregpereira.hunter.domain.monster.lore

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.single

@OptIn(ExperimentalCoroutinesApi::class)
class SyncMonstersLoreUseCase(
    private val repository: MonsterLoreRepository,
    private val saveMonstersLore: SaveMonstersLoreUseCase,
    private val alternativeSourceRepository: MonsterLoreSourceRepository,
    private val settingsRepository: MonsterLoreSettingsRepository,
    private val getMonstersLoreEdited: GetMonstersLoreEdited,
) {

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
            .removeMonstersLoreEdited()
            .flatMapLatest { monstersLore ->
                saveMonstersLore(monstersLore, isSync = true)
            }
    }

    private fun Flow<List<MonsterLore>>.removeMonstersLoreEdited(): Flow<List<MonsterLore>> {
        return map { monstersLore ->
            val monstersLoreEditedIndexes = getMonstersLoreEdited().single()
                .map { it.index }
                .toSet()
            monstersLore.filterNot { it.index in monstersLoreEditedIndexes }
        }
    }
}
