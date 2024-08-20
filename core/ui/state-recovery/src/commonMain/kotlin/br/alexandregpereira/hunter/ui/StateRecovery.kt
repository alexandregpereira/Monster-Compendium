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

package br.alexandregpereira.hunter.ui

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow

interface StateRecovery : MutableMap<String, Any?> {
    val onStateChanges: Flow<Map<String, Any?>>

    fun save(state: Map<String, Any?>)

    fun dispatchChanges()
}

fun StateRecovery(): StateRecovery = ReactiveStateRecovery(
    stateRecovery = MemoryStateRecoveryImpl()
)

private class MemoryStateRecoveryImpl : StateRecovery {

    override val onStateChanges: Flow<Map<String, Any?>> = emptyFlow()

    private var _state: MutableMap<String, Any?> = mutableMapOf()

    override fun save(state: Map<String, Any?>) {
        _state = state.toMutableMap()
    }

    override fun dispatchChanges() {
        // empty
    }

    override fun get(key: String): Any? = _state[key]

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = _state.entries

    override val keys: MutableSet<String>
        get() = _state.keys

    override val size: Int
        get() = _state.size

    override val values: MutableCollection<Any?>
        get() = _state.values

    override fun clear() = _state.clear()

    override fun containsKey(key: String): Boolean =_state.containsKey(key)

    override fun containsValue(value: Any?): Boolean = _state.containsValue(value)

    override fun isEmpty(): Boolean = _state.isEmpty()

    override fun put(key: String, value: Any?): Any? = _state.put(key, value)

    override fun putAll(from: Map<out String, Any?>) {
        from.forEach { (key, value) -> put(key, value) }
    }

    override fun remove(key: String): Any? = _state.remove(key)

    override fun toString(): String = _state.toString()
}

private class ReactiveStateRecovery(
    private val stateRecovery: StateRecovery
) : StateRecovery by stateRecovery {

    private val dispatcher: MutableSharedFlow<Map<String, Any?>> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val onStateChanges: Flow<Map<String, Any?>> = dispatcher.asSharedFlow()

    override fun save(state: Map<String, Any?>) {
        stateRecovery.save(state)
    }

    override fun dispatchChanges() {
        dispatcher.tryEmit(stateRecovery)
    }

    override fun toString(): String = stateRecovery.toString()
}
