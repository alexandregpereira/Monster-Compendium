package br.alexandregpereira.hunter.monster.compendium.state

internal sealed class MonsterCompendiumException(message: String) : Throwable(message) {

    data class NavigateToCompendiumIndexError(
        val monsterIndex: String
    ) : MonsterCompendiumException(message = "Monster index $monsterIndex not found")
}
