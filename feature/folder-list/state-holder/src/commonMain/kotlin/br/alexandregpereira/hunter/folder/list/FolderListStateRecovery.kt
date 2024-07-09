package br.alexandregpereira.hunter.folder.list

import br.alexandregpereira.hunter.ui.StateRecovery

internal fun StateRecovery.getState(): FolderListState {
    return FolderListState(
        isItemSelectionOpen = this["folderList:isItemSelectionOpen"] as? Boolean ?: false,
        itemSelectionCount = this["folderList:itemSelectionCount"] as? Int ?: 0,
        folders = (this["folderList:selectedFolders"] as? Set<*>)?.map {
            FolderCardState(
                folderName = it as String,
                selected = true,
            )
        } ?: emptyList(),
    )
}

internal fun FolderListState.saveState(stateRecovery: StateRecovery): FolderListState {
    stateRecovery["folderList:isItemSelectionOpen"] = isItemSelectionOpen
    stateRecovery["folderList:itemSelectionCount"] = itemSelectionCount
    stateRecovery["folderList:selectedFolders"] = itemSelection
    stateRecovery.dispatchChanges()
    return this
}
