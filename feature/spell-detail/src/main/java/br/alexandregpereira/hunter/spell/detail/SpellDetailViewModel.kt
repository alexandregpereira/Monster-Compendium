package br.alexandregpereira.hunter.spell.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class SpellDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSpell: GetSpellUseCase,
    private val spellDetailEventListener: SpellDetailEventListener,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var spellIndex: String
        get() = savedStateHandle["spellIndex"] ?: ""
        set(value) {
            savedStateHandle["spellIndex"] = value
        }

    private val _state = MutableStateFlow(SpellDetailViewState.INITIAL)
    val state: StateFlow<SpellDetailViewState> = _state

    init {
        loadSpell(spellIndex)
        observeEvents()
    }

    private fun loadSpell(spellIndex: String) {
        getSpell(spellIndex)
            .map { spell -> spell.asState() }
            .flowOn(dispatcher)
            .onEach { spell ->
                _state.value = state.value.changeSpell(spell)
            }
            .catch {}
            .launchIn(viewModelScope)
    }

    private fun observeEvents() {
        spellDetailEventListener.events
            .onEach { event ->
                when (event) {
                    is SpellDetailEvent.ShowSpell -> {
                        spellIndex = event.index
                        loadSpell(event.index)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onClose() {
        spellIndex = ""
        _state.value = state.value.hideDetail()
    }
}
