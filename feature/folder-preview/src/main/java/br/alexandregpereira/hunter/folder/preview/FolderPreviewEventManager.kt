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

package br.alexandregpereira.hunter.folder.preview

import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class FolderPreviewEventManager :
    FolderPreviewEventDispatcher,
    FolderPreviewResultListener {

    private val _events: MutableSharedFlow<FolderPreviewEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events: Flow<FolderPreviewEvent> = _events

    private val _result: MutableSharedFlow<FolderPreviewResult> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val result: Flow<FolderPreviewResult> = _result

    override fun dispatchEvent(event: FolderPreviewEvent) {
        _events.tryEmit(event)
    }

    fun dispatchResult(result: FolderPreviewResult) {
        _result.tryEmit(result)
    }
}
