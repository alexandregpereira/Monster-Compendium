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

package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.domain.settings.GetContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.IsLanguageSupported
import br.alexandregpereira.hunter.domain.settings.SaveContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import java.util.Locale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

@OptIn(ExperimentalCoroutinesApi::class)
class HandleSyncUseCase internal constructor(
    private val sync: SyncUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getContentVersionUseCase: GetContentVersionUseCase,
    private val saveContentVersionUseCase: SaveContentVersionUseCase
) {

    private val contentVersion = 1

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return isToSync().flatMapLatest { isToSync ->
            if (isToSync) sync() else flowOf(Unit)
        }
    }

    private fun isToSync(): Flow<Boolean> {
        return isLangSyncScenario()
            .zip(isContentVersionSyncScenario()) { isLangSyncScenario, isContentVersionSyncScenario ->
                isLangSyncScenario || isContentVersionSyncScenario
            }
    }

    private fun isLangSyncScenario(): Flow<Boolean> {
        return getLanguageUseCase().flatMapLatest { lang ->
            val deviceLang = Locale.getDefault().toLanguageTag().lowercase()
            if (deviceLang != lang && IsLanguageSupported(deviceLang)) {
                saveLanguageUseCase(deviceLang).map {
                    true
                }
            } else flowOf(false)
        }
    }

    private fun isContentVersionSyncScenario(): Flow<Boolean> {
        return getContentVersionUseCase().flatMapLatest { lastContentVersion ->
            if (contentVersion != lastContentVersion) {
                saveContentVersionUseCase(contentVersion).map {
                    true
                }
            } else flowOf(false)
        }
    }
}
