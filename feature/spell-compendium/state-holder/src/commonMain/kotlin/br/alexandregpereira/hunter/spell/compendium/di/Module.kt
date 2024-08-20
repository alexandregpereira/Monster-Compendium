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

package br.alexandregpereira.hunter.spell.compendium.di

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumStateHolder
import br.alexandregpereira.hunter.spell.compendium.domain.GetSpellsUseCase
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

val spellCompendiumModule = module {
    single { SpellCompendiumEventManager() }

    factory<SpellCompendiumEventResultDispatcher> { get<SpellCompendiumEventManager>() }

    factory { GetSpellsUseCase(repository = get()) }

    single {
        SpellCompendiumStateHolder(
            dispatcher = Dispatchers.Default,
            getSpellsUseCase = get(),
            eventListener = get<SpellCompendiumEventManager>(),
            resultDispatcher = get<SpellCompendiumEventManager>(),
            appLocalization = get(),
        )
    }
}

private class SpellCompendiumEventManager :
    EventListener<SpellCompendiumEvent>,
    EventDispatcher<SpellCompendiumResult>,
    SpellCompendiumEventResultDispatcher {

    private val eventDelegate = EventManager<SpellCompendiumEvent>()
    private var resultDelegate = EventManager<SpellCompendiumResult>()

    override val events: Flow<SpellCompendiumEvent> = eventDelegate.events

    override fun dispatchEvent(event: SpellCompendiumResult) {
        resultDelegate.dispatchEvent(event)
    }

    override fun dispatchEventResult(event: SpellCompendiumEvent): Flow<SpellCompendiumResult> {
        resultDelegate = EventManager()
        eventDelegate.dispatchEvent(event)
        return resultDelegate.events
    }
}
