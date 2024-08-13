package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.app.BottomBarItemIcon.COMPENDIUM
import br.alexandregpereira.hunter.app.BottomBarItemIcon.FOLDERS
import br.alexandregpereira.hunter.app.BottomBarItemIcon.SEARCH
import br.alexandregpereira.hunter.app.BottomBarItemIcon.SETTINGS
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.app.event.AppEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MainViewModel(
    private val appLocalization: AppReactiveLocalization,
    private val stateRecovery: StateRecovery,
    appEventDispatcher: AppEventDispatcher,
) : UiModel<MainViewState>(MainViewState()) {

    init {
        setState { updateState(stateRecovery) }
        observeLanguageChanges()
        appEventDispatcher.observeEvents()
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach { language ->
            setState {
                val strings = language.getStrings()
                copy(
                    bottomBarItems = BottomBarItemIcon.entries.map {
                        when (it) {
                            COMPENDIUM -> BottomBarItem(
                                icon = it,
                                text = strings.compendium
                            )
                            SEARCH -> BottomBarItem(
                                icon = it,
                                text = strings.search
                            )
                            FOLDERS -> BottomBarItem(
                                icon = it,
                                text = strings.folders
                            )
                            SETTINGS -> BottomBarItem(
                                icon = it,
                                text = strings.menu
                            )
                        }
                    },
                )
            }
        }.launchIn(scope)
    }

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is BottomNavigationItemClick -> {
                setStateAndSave { copy(bottomBarItemSelectedIndex = bottomBarItems.indexOf(event.item)) }
            }
        }
    }

    private fun setStateAndSave(block: MainViewState.() -> MainViewState) {
        setState { block().saveState(stateRecovery) }
    }

    private fun MainViewState.saveState(stateRecovery: StateRecovery): MainViewState {
        stateRecovery["app:bottomBarItemSelectedIndex"] = bottomBarItemSelectedIndex
        stateRecovery.dispatchChanges()
        return this
    }

    private fun MainViewState.updateState(bundle: Map<String, Any?>): MainViewState {
        return copy(
            bottomBarItemSelectedIndex = bundle["app:bottomBarItemSelectedIndex"] as? Int ?: 0,
        )
    }
}
