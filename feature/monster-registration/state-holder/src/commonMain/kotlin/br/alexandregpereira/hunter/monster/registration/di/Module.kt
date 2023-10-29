package br.alexandregpereira.hunter.monster.registration.di

import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationParams
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationStateHolder
import br.alexandregpereira.hunter.monster.registration.di.MonsterRegistrationQualifiers.eventManagerQualifier
import br.alexandregpereira.hunter.monster.registration.di.MonsterRegistrationQualifiers.paramsQualifier
import br.alexandregpereira.hunter.monster.registration.domain.NormalizeMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventDispatcher
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.state.StateHolderParams
import kotlinx.coroutines.Dispatchers
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
    single<MonsterRegistrationResultManager> {
        MonsterRegistrationResultManager()
    }
    single<MonsterRegistrationEventListener> {
        get<MonsterRegistrationResultManager>()
    }
    factory { NormalizeMonsterUseCase() }

    factory {
        MonsterRegistrationStateHolder(
            params = get(qualifier = paramsQualifier),
            eventManager = get(qualifier = eventManagerQualifier),
            eventResultManager = get<MonsterRegistrationResultManager>(),
            dispatcher = Dispatchers.Default,
            getMonster = get(),
            saveMonsters = get(),
            normalizeMonster = get(),
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

internal class MonsterRegistrationResultManager: MonsterRegistrationEventListener,
    EventManager<MonsterRegistrationResult> by EventManager()
