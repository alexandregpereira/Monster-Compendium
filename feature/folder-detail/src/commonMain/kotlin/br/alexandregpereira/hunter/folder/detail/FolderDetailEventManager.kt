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

package br.alexandregpereira.hunter.folder.detail

import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResult
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResultListener
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class FolderDetailEventManager : FolderDetailEventDispatcher,
    FolderDetailResultListener {

    private val _events: MutableSharedFlow<FolderDetailEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events: Flow<FolderDetailEvent> = _events

    private val _result: MutableSharedFlow<FolderDetailResult> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val result: Flow<FolderDetailResult> = _result

    override fun dispatchEvent(event: FolderDetailEvent) {
        _events.tryEmit(event)
    }

    fun dispatchResult(result: FolderDetailResult) {
        _result.tryEmit(result)
    }
}
