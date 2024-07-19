package br.alexandregpereira.hunter.shareContent.state

internal sealed class ShareContentUiEvent {
    data class CopyContentUiToExport(val content: String) : ShareContentUiEvent()
}
