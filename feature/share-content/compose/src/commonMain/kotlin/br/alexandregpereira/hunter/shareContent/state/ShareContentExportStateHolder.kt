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

package br.alexandregpereira.hunter.shareContent.state

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent.Export
import br.alexadregpereira.hunter.shareContent.event.exportEvents
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContent
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileManager
import br.alexandregpereira.hunter.shareContent.domain.GetShareContent
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Clock

internal class ShareContentExportStateHolder(
    private val dispatcher: CoroutineDispatcher,
    private val analytics: Analytics,
    private val appLocalization: AppLocalization,
    private val compendiumFileManager: CompendiumFileManager,
    private val getShareContent: GetShareContent,
    private val clock: Clock = Clock.System,
    eventDispatcher: EventDispatcher<ShareContentEvent>,
) : UiModel<ShareContentExportState>(
    initialState = ShareContentExportState(),
), MutableActionHandler<ShareContentExportUiEvent> by MutableActionHandler() {

    private var compendiumFileContent: CompendiumFileContent? = null

    init {
        eventDispatcher.exportEvents().onEach { event ->
            when (event) {
                is Export.OnStart -> {
                    analytics.track(
                        eventName = "Export content - opened",
                        params = mapOf("monsterQuantity" to event.monsterIndexes.size),
                    )
                    setState {
                        copy(
                            isOpen = true,
                            isLoading = true,
                            strings = appLocalization.getLanguage().getExportStrings(),
                        )
                    }
                    fetchMonsterContentToExport(event.monsterIndexes)
                }
            }
        }.launchIn(scope)
    }

    fun onClose() {
        analytics.track(
            eventName = "Export content - closed",
        )
        featureScope.launch {
            withContext(dispatcher) {
                compendiumFileManager.deleteCompendiumFiles()
            }
            onCleared()
            setState { copy(isOpen = false) }
        }
    }

    fun onExportToFile() {
        analytics.track(
            eventName = "Export content - export to file",
        )
        val compendiumFileContent = compendiumFileContent
        if (compendiumFileContent == null) {
            analytics.logException(IllegalStateException("contentToExport must not be null"))
            return
        }
        featureScope.launch {
            val filePath = withContext(dispatcher) {
                compendiumFileManager.createCompendiumFile(compendiumFileContent)
            }
            sendAction(ShareContentExportUiEvent.ShareFile(filePath))
            setState { copy(isOpen = false) }
        }
    }

    private fun fetchMonsterContentToExport(
        monsterIndexes: List<String>,
    ) {
        flow {
            emit(getShareContent(monsterIndexes))
        }.map { shareContent ->
            val timestamp = clock.now().epochSeconds
            val fileName = "content-$timestamp.compendium"
            compendiumFileManager.getCompendiumFileContent(
                fileName = fileName,
                shareContent = shareContent,
            )
        }.onEach {
            compendiumFileManager.deleteCompendiumFiles()
        }.flowOn(dispatcher)
            .catch { cause ->
                analytics.logException(cause)
            }
            .onEach { compendiumFileContent ->
                this.compendiumFileContent = compendiumFileContent
                setState {
                    copy(
                        isLoading = false,
                        exportExtractedState = compendiumFileContent.toShareContentExtractedState(
                            strings = strings.extractedStrings
                        )
                    )
                }
            }
            .launchIn(featureScope)
    }
}
