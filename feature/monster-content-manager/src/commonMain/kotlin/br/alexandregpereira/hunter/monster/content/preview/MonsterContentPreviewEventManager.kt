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

package br.alexandregpereira.hunter.monster.content.preview

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class MonsterContentPreviewEventManager {

    private val _events: MutableSharedFlow<MonsterContentPreviewEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: Flow<MonsterContentPreviewEvent> = _events

    private fun dispatchEvent(event: MonsterContentPreviewEvent) {
        _events.tryEmit(event)
    }

    fun show(sourceAcronym: String, title: String) {
        dispatchEvent(MonsterContentPreviewEvent.Show(sourceAcronym, title))
    }
}

internal sealed class MonsterContentPreviewEvent {
    data class Show(val sourceAcronym: String, val title: String) : MonsterContentPreviewEvent()
}
