package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.Monster

internal fun Analytics.trackMonsterRegistrationOpened(monsterIndex: String) {
    track(
        eventName = "MonsterRegistration - opened",
        params = mapOf(
            "monsterIndex" to monsterIndex,
        ),
    )
}

internal fun Analytics.trackMonsterRegistrationClosed(monsterIndex: String) {
    track(
        eventName = "MonsterRegistration - closed",
        params = mapOf(
            "monsterIndex" to monsterIndex,
        ),
    )
}

internal fun Analytics.trackMonsterRegistrationSaved(monster: Monster) {
    track(
        eventName = "MonsterRegistration - saved",
        params = mapOf(
            "monsterIndex" to monster.index,
            "monster" to monster.toString(),
        ),
    )
}
