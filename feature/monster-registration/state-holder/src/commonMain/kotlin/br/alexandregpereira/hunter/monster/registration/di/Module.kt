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

package br.alexandregpereira.hunter.monster.registration.di

import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationParams
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationStateHolder
import br.alexandregpereira.hunter.monster.registration.di.MonsterRegistrationQualifiers.eventManagerQualifier
import br.alexandregpereira.hunter.monster.registration.di.MonsterRegistrationQualifiers.paramsQualifier
import br.alexandregpereira.hunter.monster.registration.domain.NormalizeMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.domain.SaveMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.domain.SaveMonsterUseCaseImpl
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventDispatcher
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.state.StateHolderParams
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val monsterRegistrationModule = module {
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
    factory<SaveMonsterUseCase> {
        SaveMonsterUseCaseImpl(
            saveMonsters = get(),
            monsterImageRepository = get(),
            saveMonstersLoreUseCase = get(),
        )
    }

    single {
        MonsterRegistrationStateHolder(
            params = get(qualifier = paramsQualifier),
            eventManager = get(qualifier = eventManagerQualifier),
            eventResultManager = get<MonsterRegistrationResultManager>(),
            dispatcher = Dispatchers.Default,
            getMonster = get(),
            getMonsterLore = get(),
            saveMonster = get(),
            normalizeMonster = get(),
            analytics = get(),
            spellCompendiumEventDispatcher = get(),
            spellDetailEventDispatcher = get(),
            getSpell = get(),
            appLocalization = get(),
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
