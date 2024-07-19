package br.alexandregpereira.hunter.shareContent.state

internal data class ShareContentState(
    val contentToImport: String = "",
    val contentToExport: String = "",
    val exportCopyButtonEnabled: Boolean = false,
    val importError: ShareContentImportError? = null,
    val strings: ShareContentStrings = ShareContentStrings(),
) {
    val exportCopyButtonText: String = strings.copyButton.takeIf { exportCopyButtonEnabled }
        ?: strings.copiedButton

    val importErrorMessage: String = importError?.let {
        when (it) {
            ShareContentImportError.InvalidContent -> strings.importInvalidContentErrorMessage
        }
    } ?: ""

    val contentToExportShort: String = contentToExport.take(1000)
        .takeIf { it.isNotBlank() }?.let { "$it..." }.orEmpty()

    val contentToImportShort: String = contentToImport.take(1000)
        .takeIf { it.isNotBlank() }?.let { "$it..." }.orEmpty()
}

internal enum class ShareContentImportError {
    InvalidContent
}
