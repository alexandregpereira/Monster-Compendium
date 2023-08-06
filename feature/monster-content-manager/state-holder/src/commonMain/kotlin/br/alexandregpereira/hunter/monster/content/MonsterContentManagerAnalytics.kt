package br.alexandregpereira.hunter.monster.content

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource

internal class MonsterContentManagerAnalytics(
    private val analytics: Analytics
) {

    fun trackMonsterContentLoaded(monsterContents: List<AlternativeSource>) {
        analytics.track(
            eventName = "MonsterContentManager - monster contents loaded",
            params = mapOf(
                "monsterContents" to monsterContents.map { it.originalName }.toString(),
            )
        )
    }

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackAddContentClick(acronym: String) {
        analytics.track(
            eventName = "MonsterContentManager - add content click",
            params = mapOf(
                "acronym" to acronym,
            )
        )
    }

    fun trackRemoveContentClick(acronym: String) {
        analytics.track(
            eventName = "MonsterContentManager - remove content click",
            params = mapOf(
                "acronym" to acronym,
            )
        )
    }

    fun trackPreviewContentClick(acronym: String, name: String) {
        analytics.track(
            eventName = "MonsterContentManager - preview click",
            params = mapOf(
                "acronym" to acronym,
                "contentName" to name,
            )
        )
    }

    fun trackClose() {
        analytics.track(eventName = "MonsterContentManager - close")
    }
}