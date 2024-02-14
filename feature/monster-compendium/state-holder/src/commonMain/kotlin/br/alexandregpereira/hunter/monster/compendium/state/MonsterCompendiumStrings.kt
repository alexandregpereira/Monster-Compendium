package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterCompendiumStrings {
    val noInternetConnection: String
    val tryAgain: String
}

internal data class MonsterCompendiumEnStrings(
    override val noInternetConnection: String = "No internet connection",
    override val tryAgain: String = "Try again"
) : MonsterCompendiumStrings

internal data class MonsterCompendiumPtrStrings(
    override val noInternetConnection: String = "Sem conexão com a internet",
    override val tryAgain: String = "Tentar novamente"
) : MonsterCompendiumStrings

fun MonsterCompendiumStrings(): MonsterCompendiumStrings = MonsterCompendiumEnStrings()

internal fun AppLocalization.getStrings(): MonsterCompendiumStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterCompendiumEnStrings()
        Language.PORTUGUESE -> MonsterCompendiumPtrStrings()
    }
}
