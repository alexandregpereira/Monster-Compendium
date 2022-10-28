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

package br.alexandregpereira.hunter.folder.insert

import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class FolderInsertEventManager : FolderInsertEventDispatcher, FolderInsertResultListener {

    private val _events: MutableSharedFlow<FolderInsertEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events: Flow<FolderInsertEvent> = _events

    private val _result: MutableSharedFlow<FolderInsertResult> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val result: Flow<FolderInsertResult> = _result

    private lateinit var eventResult: MutableSharedFlow<FolderInsertResult>

    override fun dispatchEvent(event: FolderInsertEvent): Flow<FolderInsertResult> {
        eventResult = MutableSharedFlow(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        _events.tryEmit(event)
        return eventResult
    }

    fun dispatchResult(result: FolderInsertResult) {
        eventResult.tryEmit(result)
        _result.tryEmit(result)
    }
}
