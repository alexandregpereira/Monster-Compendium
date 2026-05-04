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
import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.shareContent.domain.ContentToExport
import br.alexandregpereira.hunter.shareContent.domain.ExportMonstersContentToFile
import br.alexandregpereira.hunter.shareContent.domain.GetMonstersContentToExport
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ShareContentExportStateHolder(
    private val dispatcher: CoroutineDispatcher,
    private val getMonstersContentToExport: GetMonstersContentToExport,
    private val exportMonstersContentToFile: ExportMonstersContentToFile,
    private val analytics: Analytics,
    private val fileManager: FileManager,
    eventDispatcher: EventDispatcher<ShareContentEvent>,
) : UiModel<ShareContentExportState>(
    initialState = ShareContentExportState(),
), MutableActionHandler<ShareContentExportUiEvent> by MutableActionHandler() {

    private var contentToExport: ContentToExport? = null

    init {
        eventDispatcher.exportEvents().onEach { event ->
            when (event) {
                is Export.OnStart -> {
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
                fileManager.deleteAllsFilesFromAppStorage(FileType.ZIP)
            }
            onCleared()
        }
    }

    private fun fetchMonsterContentToExport(
        monsterIndexes: List<String>,
    ) {
        getMonstersContentToExport(monsterIndexes)
            .onEach {
                fileManager.deleteAllsFilesFromAppStorage(FileType.ZIP)
            }
            .flowOn(dispatcher)
            .onEach { contentToExport ->
                this.contentToExport = contentToExport
                val filePath = exportToFile(contentToExport)
                analytics.track(
                    eventName = "Export content - opened",
                    params = mapOf("monsterQuantity" to monsterIndexes.size),
                )
                sendAction(ShareContentExportUiEvent.ShareFile(filePath))
            }
            .launchIn(featureScope)
    }

    private suspend fun exportToFile(
        contentToExport: ContentToExport,
    ): String = exportMonstersContentToFile(contentToExport)
}
