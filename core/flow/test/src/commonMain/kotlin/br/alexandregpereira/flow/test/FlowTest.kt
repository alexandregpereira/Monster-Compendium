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

package br.alexandregpereira.flow.test

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun <A> TestScope.testFlow(
    flow: Flow<A>,
    block: () -> Unit
): MutableList<A> {
    val values = mutableListOf<A>()
    val job = launch(start = CoroutineStart.UNDISPATCHED, context = Dispatchers.Unconfined) {
        flow.collect {
            values.add(it)
        }
    }
    block()
    advanceUntilIdle()

    job.cancel()
    return values
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <A, B> TestScope.testFlows(
    flowA: Flow<A>,
    flowB: Flow<B>,
    awaitInitialState: Boolean = false,
    block: () -> Unit = {},
): Pair<MutableList<A>, MutableList<B>> {
    if (awaitInitialState) {
        advanceUntilIdle()
    }
    var valuesB = mutableListOf<B>()
    val valuesA = testFlow(flowA) {
        valuesB = testFlow(flowB, block)
    }

    return valuesA to valuesB
}

fun <A, B, C> TestScope.testFlows(
    flowA: Flow<A>,
    flowB: Flow<B>,
    flowC: Flow<C>,
    awaitInitialState: Boolean = false,
    block: () -> Unit
): Triple<MutableList<A>, MutableList<B>, MutableList<C>> {
    var valuesC = mutableListOf<C>()

    val (valuesA, valuesB) = testFlows(flowA, flowB, awaitInitialState) {
        valuesC = testFlow(flowC, block)
    }

    return Triple(valuesA, valuesB, valuesC)
}

infix fun <A, B, C> Triple<MutableList<A>, MutableList<B>, MutableList<C>>.then(
    block: (valuesA: MutableList<A>, valuesB: MutableList<B>, valuesC: MutableList<C>) -> Unit
) {
    val (valuesA, valuesB, valuesC) = this
    block(valuesA, valuesB, valuesC)
}

fun <Value> MutableList<Value>.assertNextValue(value: Value) {
    if (this.isEmpty()) {
        throw AssertionError("Expected the value $value but was empty")
    }
    assertEquals(expected = value, actual = this.first())
    this.removeFirst()
}

fun <Value> MutableList<Value>.assertFinalValue(value: Value) {
    assertNextValue(value)
    assertHasNoMoreValues()
}

fun <Value> List<Value>.assertHasNoMoreValues() {
    assertEquals(expected = emptyList(), actual = this)
}