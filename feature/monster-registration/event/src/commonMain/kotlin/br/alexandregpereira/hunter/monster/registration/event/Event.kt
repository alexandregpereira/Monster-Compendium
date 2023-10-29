package br.alexandregpereira.hunter.monster.registration.event

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.event.EventManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class MonsterRegistrationEvent {

    data class ShowEdit(val monsterIndex: String) : MonsterRegistrationEvent()

    data object Hide : MonsterRegistrationEvent()
}

sealed class MonsterRegistrationResult {
    data object OnSaved : MonsterRegistrationResult()
}

interface MonsterRegistrationEventListener : EventListener<MonsterRegistrationResult>

interface MonsterRegistrationEventDispatcher : EventDispatcher<MonsterRegistrationEvent>

fun EventListener<MonsterRegistrationResult>.collectOnSaved(
    onAction: () -> Unit
): Flow<Unit> = events.map { it is MonsterRegistrationResult.OnSaved }.map { onAction() }