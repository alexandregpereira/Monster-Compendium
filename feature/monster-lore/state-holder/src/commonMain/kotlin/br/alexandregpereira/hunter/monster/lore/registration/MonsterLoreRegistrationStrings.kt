package br.alexandregpereira.hunter.monster.lore.registration

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterLoreRegistrationStrings {
    val title: String
}

private data class MonsterLoreRegistrationEnStrings(
    override val title: String = "Monster Lore Registration",
) : MonsterLoreRegistrationStrings

private data class MonsterLoreRegistrationPtBrStrings(
    override val title: String = "Registro de Mitologia",
) : MonsterLoreRegistrationStrings

fun MonsterLoreRegistrationStrings(): MonsterLoreRegistrationStrings = MonsterLoreRegistrationEnStrings()

internal fun AppLocalization.getStrings(): MonsterLoreRegistrationStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterLoreRegistrationEnStrings()
        Language.PORTUGUESE -> MonsterLoreRegistrationPtBrStrings()
    }
}
