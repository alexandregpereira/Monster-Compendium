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

import br.alexandregpereira.hunter.domain.monster.lore.SyncMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.settings.GetContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.SaveContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import br.alexandregpereira.hunter.domain.spell.SyncSpellsUseCase
import br.alexandregpereira.hunter.domain.sync.model.SyncStatus
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
    private val getContentVersionUseCase: GetContentVersionUseCase,
    private val saveContentVersionUseCase: SaveContentVersionUseCase,
    private val isFirstTime: IsFirstTime,
    private val resetFirstTime: ResetFirstTime,
) {

    private val contentVersion = 4

    operator fun invoke(forceSync: Boolean = true): Flow<SyncStatus> {
        return flow {
            val (isToSync, lastLanguageRollback, lastContentVersionRollback) = isToSync().single()
            if (forceSync || isToSync) {
                emit(SyncStatus.BUSY)
                coroutineScope {
                    runCatching {
                        awaitAll(
                            async { syncMonsters().single() },
                            async { syncSpells().single() },
                            async { syncMonstersLoreUseCase().single() },
                        )
                    }.onFailure {
                        runCatching { saveLanguageUseCase(lastLanguageRollback).single() }
                        runCatching { saveContentVersionUseCase(lastContentVersionRollback).single() }
                    }
                }
                resetFirstTime()
                emit(SyncStatus.SYNCED)
            } else {
                emit(SyncStatus.IDLE)
            }
        }
    }

    private fun isToSync(): Flow<Triple<Boolean, String, Int>> {
        return isLangSyncScenario()
            .zip(isContentVersionSyncScenario()) { (isLangSyncScenario, lastLanguageRollback), (isContentVersionSyncScenario, lastContentVersionRollback) ->
                val isToSync = isFirstTime() || isLangSyncScenario || isContentVersionSyncScenario
                Triple(isToSync, lastLanguageRollback, lastContentVersionRollback)
            }
    }

    private fun isLangSyncScenario(): Flow<Pair<Boolean, String>> {
        return getLanguageUseCase().map { lang ->
            false to lang
        }
    }

    private fun isContentVersionSyncScenario(): Flow<Pair<Boolean, Int>> {
        return getContentVersionUseCase().flatMapLatest { lastContentVersion ->
            if (contentVersion != lastContentVersion) {
                saveContentVersionUseCase(contentVersion).map {
                    true to lastContentVersion
                }
            } else flowOf(false to lastContentVersion)
        }
    }
}
