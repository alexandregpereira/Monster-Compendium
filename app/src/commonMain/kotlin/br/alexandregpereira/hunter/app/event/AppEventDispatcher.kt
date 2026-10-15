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

package br.alexandregpereira.hunter.app.event

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface AppEventDispatcher {
    fun observeEvents()
    fun onFileOpen(filePath: String)
}

internal class AppEventDispatcherImpl(
    private val shareContentEventDispatcher: ShareContentEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
) : AppEventDispatcher {

    private val scope = MainScope()

    override fun observeEvents() {
        observeShareContentEvents()
    }

    override fun onFileOpen(filePath: String) {
        shareContentEventDispatcher.dispatchEvent(
            ShareContentEvent.Import.OnStart(
                compendiumFilePath = filePath,
            )
        )
    }

    private fun observeShareContentEvents() {
        shareContentEventDispatcher.events.filterIsInstance<ShareContentEvent.Import.OnFinish>()
            .onEach { event ->
                monsterEventDispatcher.dispatchEvent(MonsterEvent.OnCompendiumChanges())
                if (event.monsterIndexes.isNotEmpty()) {
                    val showEvent = MonsterEvent.OnVisibilityChanges.Show(
                        index = event.monsterIndexes.first(),
                        indexes = event.monsterIndexes,
                        enableMonsterPageChangesEventDispatch = true,
                    )
                    monsterEventDispatcher.dispatchEvent(showEvent)
                } else if (event.spellIndexes.isNotEmpty()) {
                    spellDetailEventDispatcher.dispatchEvent(
                        SpellDetailEvent.ShowSpell(event.spellIndexes.first())
                    )
                }
            }.launchIn(scope)
    }
}
