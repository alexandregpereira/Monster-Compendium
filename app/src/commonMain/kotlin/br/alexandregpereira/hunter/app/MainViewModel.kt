package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.app.BottomBarItemIcon.COMPENDIUM
import br.alexandregpereira.hunter.app.BottomBarItemIcon.FOLDERS
import br.alexandregpereira.hunter.app.BottomBarItemIcon.SEARCH
import br.alexandregpereira.hunter.app.BottomBarItemIcon.SETTINGS
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResultListener
import br.alexandregpereira.hunter.event.folder.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.folder.list.FolderListResultListener
import br.alexandregpereira.hunter.event.folder.list.collectOnItemSelectionVisibilityChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.systembar.BottomBarEvent.AddTopContent
import br.alexandregpereira.hunter.event.systembar.BottomBarEvent.RemoveTopContent
import br.alexandregpereira.hunter.event.systembar.BottomBarEventManager
import br.alexandregpereira.hunter.event.systembar.dispatchAddTopContentEvent
import br.alexandregpereira.hunter.event.systembar.dispatchRemoveTopContentEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventListener
import br.alexandregpereira.hunter.monster.content.event.collectOnVisibilityChanges
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val folderDetailResultListener: FolderDetailResultListener,
    private val folderListResultListener: FolderListResultListener,
    private val monsterContentManagerEventListener: MonsterContentManagerEventListener,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val bottomBarEventManager: BottomBarEventManager,
    private val appLocalization: AppReactiveLocalization,
    stateRecovery: StateRecovery<MainViewState>,
) : UiModel<MainViewState>(MainViewState(), stateRecovery) {

    init {
        observeBottomBarEvents()
        observeMonsterDetailEvents()
        observeFolderDetailResults()
        observeFolderListResults()
        observeMonsterContentManagerEvents()
        observeLanguageChanges()
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

    private fun observeBottomBarEvents() {
        bottomBarEventManager.events.onEach { event ->
            when (event) {
                is AddTopContent -> setStateAndSave { addTopContentStack(event.topContentId) }
                is RemoveTopContent -> setStateAndSave { removeTopContentStack(event.topContentId) }
            }
        }.launchIn(scope)
    }

    private fun observeMonsterDetailEvents() {
        monsterDetailEventListener.collectOnVisibilityChanges { event ->
            when (event) {
                is Show -> {
                    dispatchAddTopContentEvent(TopContent.MONSTER_DETAIL.name)
                    dispatchFolderPreviewEvent(show = false)
                }
                Hide -> {
                    dispatchRemoveTopContentEvent(TopContent.MONSTER_DETAIL.name)
                    dispatchFolderPreviewEvent(show = true)
                }
            }
        }.launchIn(scope)
    }

    private fun observeMonsterContentManagerEvents() {
        monsterContentManagerEventListener.collectOnVisibilityChanges { isShowing ->
            dispatchTopContentEvent(TopContent.MONSTER_CONTENT_MANAGER.name, isShowing)
        }.launchIn(scope)
    }

    private fun observeFolderDetailResults() {
        folderDetailResultListener.collectOnVisibilityChanges { result ->
            dispatchTopContentEvent(TopContent.FOLDER_DETAIL.name, result.isShowing)
        }.launchIn(scope)
    }

    private fun observeFolderListResults() {
        folderListResultListener.collectOnItemSelectionVisibilityChanges { result ->
            dispatchTopContentEvent(TopContent.FOLDER_LIST_SELECTION.name, result.isShowing)
            dispatchFolderPreviewEvent(result.isShowing.not())
        }.launchIn(scope)
    }

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is BottomNavigationItemClick -> {
                setStateAndSave { copy(bottomBarItemSelectedIndex = bottomBarItems.indexOf(event.item)) }
                dispatchFolderPreviewEvent(show = event.item.icon != SETTINGS)
            }
        }
    }

    private fun dispatchFolderPreviewEvent(show: Boolean) {
        val folderPreviewEvent = if (show) {
            FolderPreviewEvent.ShowFolderPreview
        } else {
            FolderPreviewEvent.HideFolderPreview
        }
        folderPreviewEventDispatcher.dispatchEvent(folderPreviewEvent)
    }

    private fun dispatchAddTopContentEvent(topContentId: String) {
        bottomBarEventManager.dispatchAddTopContentEvent(topContentId)
    }

    private fun dispatchRemoveTopContentEvent(topContentId: String) {
        bottomBarEventManager.dispatchRemoveTopContentEvent(topContentId)
    }

    private fun dispatchTopContentEvent(topContentId: String, show: Boolean) {
        if (show) {
            dispatchAddTopContentEvent(topContentId)
        } else {
            dispatchRemoveTopContentEvent(topContentId)
        }
    }
}

private enum class TopContent {
    MONSTER_DETAIL,
    FOLDER_DETAIL,
    MONSTER_CONTENT_MANAGER,
    FOLDER_LIST_SELECTION,
}