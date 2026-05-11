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
import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent.Import
import br.alexadregpereira.hunter.shareContent.event.importEvents
import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContent
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileManager
import br.alexandregpereira.hunter.shareContent.domain.ImportContent
import br.alexandregpereira.hunter.shareContent.domain.ImportContentException
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportError.InvalidContent
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.ktx.runCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class ShareContentImportStateHolder(
    private val dispatcher: CoroutineDispatcher,
    private val appLocalization: AppReactiveLocalization,
    private val eventDispatcher: EventDispatcher<ShareContentEvent>,
    private val importContent: ImportContent,
    private val analytics: Analytics,
    private val compendiumFileManager: CompendiumFileManager,
) : UiModel<ShareContentImportState>(
    ShareContentImportState(strings = appLocalization.getLanguage().getImportStrings())
), MutableActionHandler<ShareContentImportUiEvent> by MutableActionHandler() {

    private var compendiumFileContent: CompendiumFileContent? = null

    init {
        eventDispatcher.importEvents().filterIsInstance<Import.OnStart>().onEach { event ->
            val filePath = event.compendiumFilePath
            val compendiumFile = if (filePath != null) {
                FileEntry(path = filePath)
            } else null
            val hasCompendiumFile = compendiumFile != null
            analytics.track(
                eventName = "Import content - opened",
                params = mapOf("hasCompendiumFile" to hasCompendiumFile),
            )
            if (hasCompendiumFile) {
                setState {
                    copy(
                        isOpen = true,
                        isLoading = hasCompendiumFile,
                        strings = appLocalization.getLanguage().getImportStrings(),
                    )
                }
                extractZipAndPrepareImport(zipFile = compendiumFile)
            } else {
                setState {
                    copy(
                        isOpen = true,
                        isLoading = hasCompendiumFile,
                        strings = appLocalization.getLanguage().getImportStrings(),
                    )
                }
            }
        }.launchIn(scope)
    }

    fun onClose() {
        analytics.track(eventName = "Import content - closed")
        setState {
            copy(
                isOpen = false,
                importExtractedState = null,
                importError = null,
            )
        }
        compendiumFileContent = null
        scope.launch {
            runCatching {
                compendiumFileManager.deleteCompendiumFiles()
            }.onFailure {
                analytics.logException(it)
            }
            onCleared()
        }
    }

    fun onImport() {
        val compendiumFileContent = compendiumFileContent
        if (compendiumFileContent == null) {
            val cause = IllegalStateException("compendiumFileContent must not be null")
            analytics.logException(cause)
            return
        }
        analytics.track(
            eventName = "Import content - import",
            params = mapOf(
                "contentSize" to compendiumFileContent.sizeFormatted,
                "monstersQuantity" to compendiumFileContent.monstersQuantity,
                "monstersLoreQuantity" to compendiumFileContent.monstersLoreQuantity,
                "spellsQuantity" to compendiumFileContent.spellsQuantity,
                "imagesQuantity" to compendiumFileContent.imagesQuantity,
            )
        )
        setState { copy(isLoading = true) }
        flow {
            emit(importContent(compendiumFileContent))
        }.flowOn(dispatcher)
            .catch { cause ->
                if (cause is ImportContentException) {
                    analytics.logException(cause)
                    val importError = when (cause) {
                        is ImportContentException.InvalidContent -> InvalidContent
                    }
                    analytics.track(
                        eventName = "Import content - import failed",
                        params = mapOf(
                            "importError" to importError,
                        )
                    )
                    setState { copy(importError = importError) }
                } else throw cause
            }
            .onEach { monsterIndexes ->
                analytics.track(
                    eventName = "Import content - import finished",
                    params = mapOf(
                        "monstersQuantity" to monsterIndexes.size,
                    )
                )
                eventDispatcher.dispatchEvent(Import.OnFinish(monsterIndexes))
                setState {
                    copy(
                        importExtractedState = null,
                        importError = null,
                    )
                }
                onClose()
            }
            .launchIn(featureScope)
    }

    fun onFilePickClick() {
        analytics.track(eventName = "Import content - file pick clicked")
        setState { copy(isLoading = true) }
        sendAction(ShareContentImportUiEvent.PickFile)
    }

    fun onFilePicked(fileEntry: FileEntry?) {
        if (fileEntry == null) {
            analytics.track(
                eventName = "Import content - file pick cancelled",
            )
            setState { copy(isLoading = false) }
            return
        }
        analytics.track(
            eventName = "Import content - file picked",
            params = mapOf(
                "fileName" to fileEntry.name,
                "fileSize" to fileEntry.size.toString(),
            )
        )
        setState { copy(isLoading = true) }
        extractZipAndPrepareImport(zipFile = fileEntry)
    }

    private fun extractZipAndPrepareImport(zipFile: FileEntry) {
        flow {
            emit(compendiumFileManager.getCompendiumFileContent(zipFile))
        }.flowOn(dispatcher)
            .catch { cause ->
                analytics.logException(
                    RuntimeException("File import failed", cause)
                )
                analytics.track(
                    eventName = "Import content - invalid content",
                )
                setState { copy(importError = InvalidContent, isLoading = false) }
            }
            .onEach { compendiumFileContent ->
                this.compendiumFileContent = compendiumFileContent
                analytics.track(
                    eventName = "Import content - content extracted",
                    params = mapOf(
                        "fileName" to compendiumFileContent.name,
                        "contentTitle" to compendiumFileContent.contentInfo.contentTitle,
                        "contentSize" to compendiumFileContent.sizeFormatted,
                        "monstersQuantity" to compendiumFileContent.monstersQuantity,
                        "monstersLoreQuantity" to compendiumFileContent.monstersLoreQuantity,
                        "spellsQuantity" to compendiumFileContent.spellsQuantity,
                        "imagesQuantity" to compendiumFileContent.imagesQuantity,
                    )
                )
                setState {
                    copy(
                        importError = null,
                        isLoading = false,
                        importExtractedState = compendiumFileContent.toShareContentExtractedState(
                            strings = strings.extractedStrings,
                        )
                    )
                }
            }
            .launchIn(featureScope)
    }
}
