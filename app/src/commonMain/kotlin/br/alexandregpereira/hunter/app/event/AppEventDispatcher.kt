package br.alexandregpereira.hunter.app.event

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface AppEventDispatcher {
    fun observeEvents()
}

internal class AppEventDispatcherImpl(
    private val shareContentEventDispatcher: ShareContentEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
) : AppEventDispatcher {

    private val scope = MainScope()

    override fun observeEvents() {
        observeShareContentEvents()
    }

    private fun observeShareContentEvents() {
        shareContentEventDispatcher.events.filterIsInstance<ShareContentEvent.Import.OnFinish>()
            .onEach {
                monsterEventDispatcher.dispatchEvent(MonsterEvent.OnCompendiumChanges())
            }.launchIn(scope)
    }
}
