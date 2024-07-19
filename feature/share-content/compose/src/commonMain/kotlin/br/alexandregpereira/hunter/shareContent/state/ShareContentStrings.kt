package br.alexandregpereira.hunter.shareContent.state

import br.alexandregpereira.hunter.localization.Language

interface ShareContentStrings {
    val importButton: String
    val copyButton: String
    val copiedButton: String
    val contentToImportLabel: String
    val importTitle: String
    val exportTitle: String
    val importInvalidContentErrorMessage: String
    val pasteContent: String
}

internal data class ShareContentEnStrings(
    override val importButton: String = "Import",
    override val copyButton: String = "Copy",
    override val copiedButton: String = "Copied",
    override val contentToImportLabel: String = "Content",
    override val importTitle: String = "Import Content",
    override val exportTitle: String = "Share Content",
    override val importInvalidContentErrorMessage: String = "Invalid content",
    override val pasteContent: String = "Paste content",
) : ShareContentStrings

internal data class ShareContentPtStrings(
    override val importButton: String = "Importar",
    override val copyButton: String = "Copiar",
    override val copiedButton: String = "Copiado",
    override val contentToImportLabel: String = "Conteúdo",
    override val importTitle: String = "Importar Conteúdo",
    override val exportTitle: String = "Compartilhar Conteúdo",
    override val importInvalidContentErrorMessage: String = "Conteúdo inválido",
    override val pasteContent: String = "Colar conteúdo",
) : ShareContentStrings

fun ShareContentStrings(): ShareContentStrings = ShareContentEnStrings()

internal fun Language.getStrings(): ShareContentStrings {
    return when (this) {
        Language.ENGLISH -> ShareContentEnStrings()
        Language.PORTUGUESE -> ShareContentPtStrings()
    }
}
