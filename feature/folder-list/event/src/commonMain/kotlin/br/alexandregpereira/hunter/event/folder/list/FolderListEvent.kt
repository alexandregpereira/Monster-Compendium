package br.alexandregpereira.hunter.event.folder.list

sealed class FolderListEvent {

    data object OnFolderChanges : FolderListEvent()
}
