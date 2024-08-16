package br.alexandregpereira.hunter.folder.preview

sealed class FolderPreviewAction {
    data object ScrollToStart : FolderPreviewAction()
}
