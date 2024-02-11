package br.alexandregpereira.hunter.folder.insert

import br.alexandregpereira.hunter.localization.Language

interface FolderInsertStrings {
    val addToFolder: String
    val folderNameLabel: String
    val save: String
}

internal class FolderInsertEnStrings : FolderInsertStrings {
    override val addToFolder: String = "Add to folder"
    override val folderNameLabel: String = "Folder name"
    override val save: String = "Save"
}

internal class FolderInsertPtStrings : FolderInsertStrings {
    override val addToFolder: String = "Adicionar à pasta"
    override val folderNameLabel: String = "Nome da pasta"
    override val save: String = "Salvar"
}

internal class FolderInsertEmptyStrings : FolderInsertStrings {
    override val addToFolder: String = ""
    override val folderNameLabel: String = ""
    override val save: String = ""
}

internal fun getFolderInsertStrings(lang: Language): FolderInsertStrings {
    return when (lang) {
        Language.ENGLISH -> FolderInsertEnStrings()
        Language.PORTUGUESE -> FolderInsertPtStrings()
    }
}
