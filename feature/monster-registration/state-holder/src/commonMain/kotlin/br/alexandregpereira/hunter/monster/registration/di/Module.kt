package br.alexandregpereira.hunter.monster.registration.di

import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationParams
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationStateHolder
import br.alexandregpereira.hunter.monster.registration.di.MonsterRegistrationQualifiers.eventManagerQualifier
import br.alexandregpereira.hunter.monster.registration.di.MonsterRegistrationQualifiers.paramsQualifier
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventDispatcher
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import br.alexandregpereira.hunter.state.StateHolderParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val monsterRegistrationStateModule = module {
    single<StateHolderParams<MonsterRegistrationParams>>(qualifier = paramsQualifier) {
        StateHolderParams(MonsterRegistrationParams())
    }
    single<EventManager<MonsterRegistrationEvent>>(qualifier = eventManagerQualifier) {
        EventManager()
    }
    single<MonsterRegistrationEventDispatcher> {
        MonsterRegistrationEventDispatcherImpl(
            eventManager = get<EventManager<MonsterRegistrationEvent>>(
                qualifier = eventManagerQualifier
            )
        )
    }
    single<MonsterRegistrationEventListener> {
        MonsterRegistrationEventListenerImpl(
            eventManager = get<EventManager<MonsterRegistrationEvent>>(
                qualifier = eventManagerQualifier
            )
        )
    }

    factory {
        MonsterRegistrationStateHolder(
            params = get(qualifier = paramsQualifier),
            eventManager = get(qualifier = eventManagerQualifier),
            dispatcher = Dispatchers.Default,
            getMonster = get(),
        )
    }
}

object MonsterRegistrationQualifiers {
    val paramsQualifier: Qualifier = qualifier("MonsterRegistrationParams")
    internal val eventManagerQualifier: Qualifier = qualifier("MonsterRegistrationEventManager")
}

private class MonsterRegistrationEventDispatcherImpl(
    private val eventManager: EventManager<MonsterRegistrationEvent>
) : MonsterRegistrationEventDispatcher {

    override fun dispatchEvent(event: MonsterRegistrationEvent) {
        eventManager.dispatchEvent(event)
    }
}

private class MonsterRegistrationEventListenerImpl(
    eventManager: EventManager<MonsterRegistrationEvent>
): MonsterRegistrationEventListener {
    override val events: Flow<MonsterRegistrationEvent> = eventManager.events
}
