package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.analytics.Analytics

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

internal fun Analytics.trackMonsterRegistrationSaved(monsterIndex: String) {
    track(
        eventName = "MonsterRegistration - saved",
        params = mapOf(
            "monsterIndex" to monsterIndex,
        ),
    )
}
