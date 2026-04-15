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

package br.alexandregpereira.hunter.spell.registration.di

import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.spell.registration.SpellRegistrationStateHolder
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventDispatcher
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventListener
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationResult
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val featureSpellRegistrationModule = module {
    single(qualifier = SpellRegistrationQualifier.eventManager) {
        EventManager<SpellRegistrationEvent>()
    }
    single(qualifier = SpellRegistrationQualifier.resultManager) {
        EventManager<SpellRegistrationResult>()
    }

    single<SpellRegistrationEventDispatcher> {
        val eventManager = get<EventManager<SpellRegistrationEvent>>(
            qualifier = SpellRegistrationQualifier.eventManager
        )
        object : SpellRegistrationEventDispatcher {
            override fun dispatchEvent(event: SpellRegistrationEvent) {
                eventManager.dispatchEvent(event)
            }
        }
    }

    single<SpellRegistrationEventListener> {
        val resultManager = get<EventManager<SpellRegistrationResult>>(
            qualifier = SpellRegistrationQualifier.resultManager
        )
        object : SpellRegistrationEventListener {
            override val events = resultManager.events
        }
    }

    single {
        SpellRegistrationStateHolder(
            dispatcher = Dispatchers.Default,
            getSpell = get(),
            saveSpells = get(),
            eventManager = get(qualifier = SpellRegistrationQualifier.eventManager),
            resultManager = get(qualifier = SpellRegistrationQualifier.resultManager),
            appLocalization = get(),
            analytics = get(),
        )
    }
}

private object SpellRegistrationQualifier {
    val eventManager = qualifier("SpellRegistrationEventManager")
    val resultManager = qualifier("SpellRegistrationResultManager")
}
