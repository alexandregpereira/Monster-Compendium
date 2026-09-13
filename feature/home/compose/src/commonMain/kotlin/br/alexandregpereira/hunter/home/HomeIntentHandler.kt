package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.list.FolderListEvent
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.monster.compendium.event.MonsterCompendiumEvent
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEvent
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventDispatcher
import br.alexandregpereira.hunter.search.event.SearchEvent
import br.alexandregpereira.hunter.settings.event.SettingsEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

internal interface HomeIntentHandler {
    suspend fun onIntent(intent: HomeIntent)
}

internal class HomeIntentHandlerImpl(
    private val monsterCompendiumEventDispatcher: EventDispatcher<MonsterCompendiumEvent>,
    private val spellCompendiumEventDispatcher: SpellCompendiumEventResultDispatcher,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val spellRegistrationEventDispatcher: EventDispatcher<SpellRegistrationEvent>,
    private val searchEventDispatcher: EventDispatcher<SearchEvent>,
    private val settingsEventDispatcher: EventDispatcher<SettingsEvent>,
    private val folderListEventDispatcher: EventDispatcher<FolderListEvent>,
    private val folderDetailEventDispatcher: FolderDetailEventDispatcher,
    private val monsterContentManagerEventDispatcher: MonsterContentManagerEventDispatcher,
    private val monsterRegistrationEventDispatcher: MonsterRegistrationEventDispatcher,
) : HomeIntentHandler {

    override suspend fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.OpenMonsterCompendium -> {
                monsterCompendiumEventDispatcher.dispatchEvent(MonsterCompendiumEvent.Show)
            }
            HomeIntent.OpenSpellCompendium -> openSpellCompendium()
            HomeIntent.OpenSearch -> searchEventDispatcher.dispatchEvent(SearchEvent.Show)
            HomeIntent.OpenSettings -> settingsEventDispatcher.dispatchEvent(SettingsEvent.Show)
            HomeIntent.OpenFolderList -> folderListEventDispatcher.dispatchEvent(FolderListEvent.Show)
            is HomeIntent.OpenFolderDetail -> {
                folderDetailEventDispatcher.dispatchEvent(
                    FolderDetailEvent.Show(folderName = intent.folderName)
                )
            }
            HomeIntent.OpenExtraContentManager -> {
                monsterContentManagerEventDispatcher.dispatchEvent(MonsterContentManagerEvent.Show)
            }
            HomeIntent.CreateMonster -> {
                monsterRegistrationEventDispatcher.dispatchEvent(MonsterRegistrationEvent.Show())
            }
            HomeIntent.CreateSpell -> {
                spellRegistrationEventDispatcher.dispatchEvent(SpellRegistrationEvent.Show())
            }
        }
    }

    /**
     * Suspends while collecting the results, since the spell compendium stays open after a spell
     * click and every click must be handled. The caller cancels the collection.
     */
    private suspend fun openSpellCompendium() {
        spellCompendiumEventDispatcher.dispatchEventResult(event = SpellCompendiumEvent.Show())
            .onEach { result ->
                when (result) {
                    is SpellCompendiumResult.OnSpellClick -> {
                        spellDetailEventDispatcher.dispatchEvent(
                            SpellDetailEvent.ShowSpell(result.spellIndex)
                        )
                    }

                    is SpellCompendiumResult.OnSpellLongClick -> {
                        spellRegistrationEventDispatcher.dispatchEvent(
                            SpellRegistrationEvent.Show(result.spellIndex)
                        )
                    }
                }
            }
            .collect()
    }
}
