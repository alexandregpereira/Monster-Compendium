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

package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.condition.SyncConditions
import br.alexandregpereira.hunter.domain.monster.lore.SyncMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.settings.GetContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.SaveContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import br.alexandregpereira.hunter.domain.source.SaveAlternativeSourceContentVersionsUseCase
import br.alexandregpereira.hunter.domain.source.SyncAlternativeSourceContentVersionUseCase
import br.alexandregpereira.hunter.domain.spell.SyncSpellsUseCase
import br.alexandregpereira.hunter.domain.sync.model.SyncStatus
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import br.alexandregpereira.ktx.runCatching
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.zip

@OptIn(ExperimentalCoroutinesApi::class)
class SyncUseCase internal constructor(
    private val syncMonsters: SyncMonstersUseCase,
    private val syncSpells: SyncSpellsUseCase,
    private val syncMonstersLoreUseCase: SyncMonstersLoreUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val syncAlternativeSourceContentVersionUseCase: SyncAlternativeSourceContentVersionUseCase,
    private val saveAlternativeSourceContentVersionsUseCase: SaveAlternativeSourceContentVersionsUseCase,
    private val isFirstTime: IsFirstTime,
    private val resetFirstTime: ResetFirstTime,
    private val syncConditions: SyncConditions,
    private val getContentVersionUseCase: GetContentVersionUseCase,
    private val saveContentVersionUseCase: SaveContentVersionUseCase,
) {

    private val localContentVersion = 6

    operator fun invoke(forceSync: Boolean = true): Flow<SyncStatus> {
        return flow {
            val (
                isToSync,
                lastLanguageRollback,
                lastContentVersionRollback,
                lastLocalContentVersionRollback,
            ) = isToSync().single()
            if (forceSync || isToSync) {
                emit(SyncStatus.BUSY)
                coroutineScope {
                    runCatching {
                        awaitAll(
                            async { syncMonsters().single() },
                            async { syncSpells().single() },
                            async { syncMonstersLoreUseCase().single() },
                            async {
                                runCatching { syncConditions() }
                            },
                        )
                    }.onFailure {
                        runCatching { saveLanguageUseCase(lastLanguageRollback).single() }
                        runCatching { saveAlternativeSourceContentVersionsUseCase(lastContentVersionRollback).single() }
                        runCatching { saveContentVersionUseCase(lastLocalContentVersionRollback).single() }
                    }
                }
                resetFirstTime()
                emit(SyncStatus.SYNCED)
            } else {
                emit(SyncStatus.IDLE)
            }
        }
    }

    private fun isToSync(): Flow<IsToSyncData> {
        return isLangSyncScenario()
            .zip(isContentVersionSyncScenario()) { (isLangSyncScenario, lastLanguageRollback), (isContentVersionSyncScenario, lastContentVersionRollback) ->
                val (
                    isLocalContentVersionSyncScenario,
                    lastLocalContentVersionRollback,
                ) = isLocalContentVersionSyncScenario()
                val isToSync = isFirstTime() || isLangSyncScenario || isLocalContentVersionSyncScenario || isContentVersionSyncScenario
                IsToSyncData(
                    isToSync,
                    lastLanguageRollback,
                    lastContentVersionRollback,
                    lastLocalContentVersionRollback,
                )
            }
    }

    private fun isLangSyncScenario(): Flow<Pair<Boolean, String>> {
        return getLanguageUseCase().map { lang ->
            false to lang
        }
    }

    private fun isContentVersionSyncScenario(): Flow<SyncAlternativeSourceContentVersionUseCase.IsToSyncData> {
        return syncAlternativeSourceContentVersionUseCase()
    }

    private suspend fun isLocalContentVersionSyncScenario(): Pair<Boolean, Int> {
        val lastContentVersion = getContentVersionUseCase().single()
        return if (localContentVersion != lastContentVersion) {
            saveContentVersionUseCase(localContentVersion).single()
            true to lastContentVersion
        } else false to lastContentVersion
    }

    private data class IsToSyncData(
        val isToSync: Boolean,
        val lastLanguageRollback: String,
        val lastContentVersionRollback: Map<String, Int>,
        val lastLocalContentVersionRollback: Int,
    )
}
