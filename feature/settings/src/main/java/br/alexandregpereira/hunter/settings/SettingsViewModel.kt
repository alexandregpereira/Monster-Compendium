package br.alexandregpereira.hunter.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val getMonsterImageJsonUrl: GetMonsterImageJsonUrlUseCase,
    private val getAlternativeSourceJsonUrl: GetAlternativeSourceJsonUrlUseCase,
    private val saveUrls: SaveUrlsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state

    private val _action: MutableSharedFlow<SettingsAction> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action: SharedFlow<SettingsAction> = _action

    init {
        load()
    }

    fun onImageBaseUrlChange(value: String) {
        _state.value = state.value.copy(imageBaseUrl = value)
    }

    fun onAlternativeSourceBaseUrlChange(value: String) {
        _state.value = state.value.copy(alternativeSourceBaseUrl = value)
    }

    fun onSaveButtonClick() {
        _state.value = state.value.copy(saveButtonEnabled = false)
        saveUrls(
            imageBaseUrl = state.value.imageBaseUrl,
            alternativeSourceBaseUrl = state.value.alternativeSourceBaseUrl
        ).flowOn(dispatcher)
            .onEach {
                _action.tryEmit(SettingsAction.CloseApp)
            }
            .launchIn(viewModelScope)
    }

    private fun load() {
        getMonsterImageJsonUrl()
            .zip(getAlternativeSourceJsonUrl()) { imageBaseUrl, alternativeSourceBaseUrl ->
                state.value.copy(
                    imageBaseUrl = imageBaseUrl,
                    alternativeSourceBaseUrl = alternativeSourceBaseUrl
                )
            }
            .flowOn(dispatcher)
            .onEach { state ->
                _state.value = state
            }
            .launchIn(viewModelScope)
    }
}
