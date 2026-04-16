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

package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.domain.spell.DeleteSpell
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.spell.detail.domain.CloneSpellUseCase
import br.alexandregpereira.hunter.spell.detail.domain.ResetSpellToOriginalUseCase
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import br.alexandregpereira.hunter.spell.event.SpellResult
import br.alexandregpereira.hunter.spell.event.collectOnChanged
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventDispatcher
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.sync.event.collectSyncFinishedEvents
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class SpellDetailViewModel(
    private val getSpell: GetSpellUseCase,
    private val cloneSpell: CloneSpellUseCase,
    private val deleteSpell: DeleteSpell,
    private val resetSpellToOriginal: ResetSpellToOriginalUseCase,
    private val spellDetailEventListener: SpellDetailEventListener,
    private val spellRegistrationEventDispatcher: SpellRegistrationEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: SpellDetailAnalytics,
    private val appLocalization: AppLocalization,
    private val spellResultDispatcher: EventDispatcher<SpellResult>,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val syncEventListener: SyncEventListener,
) : UiModel<SpellDetailViewState>(SpellDetailViewState()) {

    private var spellResultJob: Job? = null
    private var syncFinishedJob: Job? = null

    init {
        observeEvents()
    }

    private fun loadSpell(spellIndex: String) {
        val strings = appLocalization.getStrings()
        getSpell(spellIndex)
            .map { spell -> spell.asState(strings) }
            .flowOn(dispatcher)
            .onEach { spell ->
                analytics.trackSpellLoaded(spell)
                setState { changeSpell(spell, strings) }
            }
            .catch {
                analytics.logException(it)
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        spellDetailEventListener.events
            .onEach { event ->
                when (event) {
                    is SpellDetailEvent.ShowSpell -> {
                        analytics.trackSpellShown(event.index)
                        observeSpellResultEvents()
                        loadSpell(event.index)
                    }
                }
            }
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackSpellClosed()
        spellResultJob?.cancel()
        syncFinishedJob?.cancel()
        setState { hideDetail() }
    }

    fun onOptions() {
        analytics.trackSpellOptionsShown()
        setState {
            copy(
                showOptions = true,
                options = spell.status.toOptions(strings),
            )
        }
    }

    fun onOptionsClosed() {
        setState { copy(showOptions = false) }
    }

    fun onOptionClicked(option: SpellDetailOption) {
        setState { copy(showOptions = false) }
        analytics.trackSpellOptionClicked(option)
        when (option) {
            SpellDetailOption.CLONE -> setState { copy(showCloneForm = true, spellCloneName = spell.name) }
            SpellDetailOption.EDIT -> spellRegistrationEventDispatcher.dispatchEvent(
                SpellRegistrationEvent.Show(state.value.spell.index)
            )
            SpellDetailOption.DELETE -> setState { copy(showDeleteConfirmation = true) }
            SpellDetailOption.RESET_TO_ORIGINAL -> setState { copy(showResetConfirmation = true) }
        }
    }

    fun onCloneFormChanged(name: String) {
        setState { copy(spellCloneName = name) }
    }

    fun onCloneFormClosed() {
        setState { copy(showCloneForm = false) }
    }

    fun onCloneFormSaved() {
        analytics.trackSpellCloneConfirmed()
        val spellIndex = state.value.spell.index
        val spellName = state.value.spellCloneName
        setState { copy(showCloneForm = false) }
        cloneSpell(spellIndex, spellName)
            .flowOn(dispatcher)
            .onEach { newIndex ->
                dispatchOnChangedResult(spellIndex = newIndex)
            }
            .catch { analytics.logException(it) }
            .launchIn(scope)
    }

    fun onDeleteConfirmed() {
        analytics.trackSpellDeleteConfirmed()
        val spellIndex = state.value.spell.index
        setState { copy(showDeleteConfirmation = false) }
        deleteSpell(spellIndex)
            .flowOn(dispatcher)
            .onEach {
                onClose()
                dispatchOnChangedResult(spellIndex)
            }
            .catch { analytics.logException(it) }
            .launchIn(scope)
    }

    fun onDeleteClosed() {
        setState { copy(showDeleteConfirmation = false) }
    }

    fun onResetConfirmed() {
        analytics.trackSpellResetConfirmed()
        val spellIndex = state.value.spell.index
        setState { copy(showResetConfirmation = false) }

        syncFinishedJob?.cancel()
        syncFinishedJob = syncEventListener.collectSyncFinishedEvents {
            dispatchOnChangedResult(spellIndex)
        }.launchIn(scope)

        resetSpellToOriginal(spellIndex)
            .flowOn(dispatcher)
            .onEach {
                syncEventDispatcher.startSync()
            }
            .catch { analytics.logException(it) }
            .launchIn(scope)
    }

    fun onResetClosed() {
        setState { copy(showResetConfirmation = false) }
    }

    private fun observeSpellResultEvents() {
        spellResultJob?.cancel()
        spellResultJob = spellResultDispatcher.collectOnChanged { result ->
            loadSpell(spellIndex = result.spellIndex)
        }.launchIn(scope)
    }

    private fun dispatchOnChangedResult(spellIndex: String) {
        spellResultDispatcher.dispatchEvent(SpellResult.OnChanged(spellIndex))
    }
}
