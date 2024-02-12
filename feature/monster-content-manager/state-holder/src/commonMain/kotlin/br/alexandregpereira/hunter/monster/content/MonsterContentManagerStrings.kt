package br.alexandregpereira.hunter.monster.content

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterContentManagerStrings {
    val title: String
    val add: String
    val remove: String
    val totalMonsters: (Int) -> String
    val preview: String
}

internal data class MonsterContentManagerEnStrings(
    override val title: String = "Manage Content",
    override val add: String = "Add",
    override val remove: String = "Remove",
    override val totalMonsters: (Int) -> String = { total -> "$total monsters" },
    override val preview: String = "Preview",
) : MonsterContentManagerStrings

internal data class MonsterContentManagerPtStrings(
    override val title: String = "Gerenciar Conteúdo",
    override val add: String = "Adicionar",
    override val remove: String = "Remover",
    override val totalMonsters: (Int) -> String = { total -> "$total monstros" },
    override val preview: String = "Prévia",
) : MonsterContentManagerStrings

data class MonsterContentManagerEmptyStrings(
    override val title: String = "",
    override val add: String = "",
    override val remove: String = "",
    override val totalMonsters: (Int) -> String = { _ -> "" },
    override val preview: String = "",
) : MonsterContentManagerStrings

internal fun AppLocalization.getStrings(): MonsterContentManagerStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterContentManagerEnStrings()
        Language.PORTUGUESE -> MonsterContentManagerPtStrings()
    }
}
