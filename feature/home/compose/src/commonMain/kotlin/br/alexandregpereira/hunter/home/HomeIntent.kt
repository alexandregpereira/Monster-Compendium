package br.alexandregpereira.hunter.home

internal sealed interface HomeIntent {
    data object OpenMonsterCompendium : HomeIntent
    data object OpenSpellCompendium : HomeIntent
    data object OpenSearch : HomeIntent
    data object OpenSettings : HomeIntent
    data object OpenFolderList : HomeIntent
    data class OpenFolderDetail(val folderName: String) : HomeIntent
    data object OpenExtraContentManager : HomeIntent
    data object CreateMonster : HomeIntent
    data object CreateSpell : HomeIntent
    data class OpenMonsterDetail(val index: String) : HomeIntent
}
