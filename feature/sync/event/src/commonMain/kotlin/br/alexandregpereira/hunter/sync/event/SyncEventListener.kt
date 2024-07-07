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

package br.alexandregpereira.hunter.sync.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

interface SyncEventListener {

    val events: Flow<SyncEvent>
}

fun SyncEventListener.collectSyncFinishedEvents(action: () -> Unit): Flow<Unit> {
    return events.filterIsInstance<SyncEvent.Finished>().map {
        action()
    }
}

fun emptySyncEventListener(): SyncEventListener {
    return object : SyncEventListener {
        override val events: Flow<SyncEvent>
            get() = emptyFlow()
    }
}
