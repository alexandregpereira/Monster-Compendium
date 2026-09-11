package br.alexandregpereira.hunter.event.folder.list

sealed class FolderListEvent {

    data object Show : FolderListEvent()

    data object OnFolderChanges : FolderListEvent()
}
