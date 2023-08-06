package br.alexandregpereira.hunter.monster.lore.detail

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore

internal class MonsterLoreDetailAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackMonsterLoreDetailLoaded(monsterLore: MonsterLore) {
        analytics.track(
            eventName = "MonsterLoreDetail - loaded",
            params = mapOf(
                "loreIndex" to monsterLore.index,
                "loreName" to monsterLore.name,
                "firstEntryTitle" to monsterLore.entries.firstOrNull()?.title.orEmpty(),
                "firstEntryDescription" to monsterLore.entries.firstOrNull()?.description.orEmpty(),
            )
        )
    }

    fun trackMonsterLoreDetailOpened(index: String) {
        analytics.track(
            eventName = "MonsterLoreDetail - opened",
            params = mapOf(
                "loreIndex" to index,
            )
        )
    }

    fun trackMonsterLoreDetailClosed() {
        analytics.track(
            eventName = "MonsterLoreDetail - closed",
        )
    }
}