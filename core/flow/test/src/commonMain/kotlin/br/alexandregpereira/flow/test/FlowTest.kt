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