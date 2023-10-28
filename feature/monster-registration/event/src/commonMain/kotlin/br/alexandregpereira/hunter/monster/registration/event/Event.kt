package br.alexandregpereira.hunter.monster.registration.event

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener

sealed class MonsterRegistrationEvent {

    data class ShowEdit(val monsterIndex: String) : MonsterRegistrationEvent()

    data object Hide : MonsterRegistrationEvent()
}

interface MonsterRegistrationEventListener : EventListener<MonsterRegistrationEvent>

interface MonsterRegistrationEventDispatcher : EventDispatcher<MonsterRegistrationEvent>