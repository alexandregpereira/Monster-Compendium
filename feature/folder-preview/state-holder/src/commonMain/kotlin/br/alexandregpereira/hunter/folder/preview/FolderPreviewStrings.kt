package br.alexandregpereira.hunter.folder.preview

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface FolderPreviewStrings {
    val save: String
}

internal data class FolderPreviewEnStrings(
    override val save: String = "Save",
) : FolderPreviewStrings

internal data class FolderPreviewPtStrings(
    override val save: String = "Salvar",
) : FolderPreviewStrings

internal data class FolderPreviewEmptyStrings(
    override val save: String = "",
) : FolderPreviewStrings

internal fun AppLocalization.getStrings(): FolderPreviewStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> FolderPreviewEnStrings()
        Language.PORTUGUESE -> FolderPreviewPtStrings()
    }
}
