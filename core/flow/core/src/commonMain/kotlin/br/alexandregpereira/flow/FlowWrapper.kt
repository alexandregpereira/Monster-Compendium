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

package br.alexandregpereira.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StateFlowWrapper<T : Any>(
    private val origin: StateFlow<T>,
    private val coroutineScope: CoroutineScope,
) : StateFlow<T> by origin {

    fun subscribe(block: (T) -> Unit) {
        origin.onEach(block).launchIn(coroutineScope)
    }
}

class SharedFlowWrapper<T : Any>(
    private val origin: SharedFlow<T>,
    private val coroutineScope: CoroutineScope,
) : SharedFlow<T> by origin {

    fun subscribe(block: (T) -> Unit) {
        origin.onEach(block).launchIn(coroutineScope)
    }
}

fun <T : Any> StateFlow<T>.wrap(
    coroutineScope: CoroutineScope
): StateFlowWrapper<T> = StateFlowWrapper(
    origin = this,
    coroutineScope = coroutineScope
)

fun <T : Any> SharedFlow<T>.wrap(
    coroutineScope: CoroutineScope
): SharedFlowWrapper<T> = SharedFlowWrapper(
    origin = this,
    coroutineScope = coroutineScope
)
