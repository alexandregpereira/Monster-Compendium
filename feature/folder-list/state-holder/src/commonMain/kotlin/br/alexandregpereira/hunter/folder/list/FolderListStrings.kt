package br.alexandregpereira.hunter.folder.list

import br.alexandregpereira.hunter.localization.Language

interface FolderListStrings {
    val title: String
    val delete: String
    val addToPreview: String
    val itemSelected: (Int) -> String
}

internal data class FolderListEnStrings(
    override val title: String = "Folders",
    override val delete: String = "Delete",
    override val addToPreview: String = "Add to Preview",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) {
            "$count item selected"
        } else {
            "$count items selected"
        }
    }
) : FolderListStrings

internal data class FolderListPtStrings(
    override val title: String = "Pastas",
    override val delete: String = "Deletar",
    override val addToPreview: String = "Adicionar ao Preview",
    override val itemSelected: (Int) -> String = { count ->
        if (count == 1) {
            "$count item selecionado"
        } else {
            "$count itens selecionados"
        }
    }
) : FolderListStrings

internal data class FolderListEmptyStrings(
    override val title: String = "",
    override val delete: String = "",
    override val addToPreview: String = "",
    override val itemSelected: (Int) -> String = { _ -> "" }
) : FolderListStrings

internal fun getFolderListStrings(lang: Language): FolderListStrings {
    return when (lang) {
        Language.ENGLISH -> FolderListEnStrings()
        Language.PORTUGUESE -> FolderListPtStrings()
    }
}
