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

package br.alexadregpereira.hunter.shareContent.event

import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class ShareContentEventDispatcher : EventDispatcher<ShareContentEvent> by EventDispatcher()

sealed class ShareContentEvent {
    sealed class Import : ShareContentEvent() {
        data object OnStart : Import()
        data object OnFinish : Import()
    }
    sealed class Export : ShareContentEvent() {
        data class OnStart(val monsterIndexes: List<String> = emptyList()) : Export()
        data object OnFinish : Export()
    }
}

fun EventListener<ShareContentEvent>.importEvents(): Flow<ShareContentEvent.Import> {
    return events.filterIsInstance()
}

fun EventListener<ShareContentEvent>.exportEvents(): Flow<ShareContentEvent.Export> {
    return events.filterIsInstance()
}
