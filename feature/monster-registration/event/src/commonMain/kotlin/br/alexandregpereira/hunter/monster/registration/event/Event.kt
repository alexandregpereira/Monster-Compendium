package br.alexandregpereira.hunter.monster.registration.event

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

sealed class MonsterRegistrationEvent {

    data class ShowEdit(val monsterIndex: String) : MonsterRegistrationEvent()

    data object Hide : MonsterRegistrationEvent()
}

sealed class MonsterRegistrationResult {
    data class OnSaved(val monsterIndex: String) : MonsterRegistrationResult()
}

interface MonsterRegistrationEventListener : EventListener<MonsterRegistrationResult>

interface MonsterRegistrationEventDispatcher : EventDispatcher<MonsterRegistrationEvent>

fun EventListener<MonsterRegistrationResult>.collectOnSaved(
    onAction: (String) -> Unit
): Flow<Unit> = events.map { it as? MonsterRegistrationResult.OnSaved }
    .map { event -> event?.let { onAction(it.monsterIndex) } }

fun emptyMonsterRegistrationEventDispatcher(): MonsterRegistrationEventDispatcher {
    return object : MonsterRegistrationEventDispatcher {
        override fun dispatchEvent(event: MonsterRegistrationEvent) {}
    }
}

fun emptyMonsterRegistrationEventListener(): MonsterRegistrationEventListener {
    return object : MonsterRegistrationEventListener {
        override val events: Flow<MonsterRegistrationResult> = emptyFlow()
    }
}