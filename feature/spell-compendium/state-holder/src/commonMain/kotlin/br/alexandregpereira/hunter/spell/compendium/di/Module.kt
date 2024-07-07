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
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
val featureSpellCompendiumModule = module {
    single { SpellCompendiumEventManager() }

    factory<SpellCompendiumEventResultDispatcher> { get<SpellCompendiumEventManager>() }

    factory { GetSpellsUseCase(repository = get()) }

    factory {
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
