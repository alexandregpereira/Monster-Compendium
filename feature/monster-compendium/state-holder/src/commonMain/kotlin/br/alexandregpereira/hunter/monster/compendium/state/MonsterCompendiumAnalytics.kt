package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.monster.compendium.domain.MonsterCompendiumError
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendium

class MonsterCompendiumAnalytics(
    private val analytics: Analytics
) {

    private var monsterCompendium: MonsterCompendium? = null

    fun trackMonsterCompendium(monsterCompendium: MonsterCompendium, scrollItemPosition: Int) {
        this.monsterCompendium = monsterCompendium
        analytics.track(
            eventName = "MonsterCompendium - monsters loaded",
            params = mapOf(
                "itemsSize" to monsterCompendium.items.size,
                "scrollItemPosition" to scrollItemPosition,
            )
        )
    }

    fun trackItemClick(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterCompendium - item click",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    private fun trackMonsterCompendiumEmpty(throwable: Throwable): Boolean {
        return if (throwable is MonsterCompendiumError.NoMonsterError) {
            analytics.track(eventName = "MonsterCompendium - empty")
            true
        } else false
    }

    fun logException(throwable: Throwable) {
        if (trackMonsterCompendiumEmpty(throwable).not()) {
            analytics.logException(throwable)
        }
    }

    fun trackItemLongClick(index: String) {
        analytics.track(
            eventName = "MonsterCompendium - item long click",
            params = mapOf(
                "monsterIndex" to index,
            )
        )
    }

    fun trackPopupOpened() {
        analytics.track(eventName = "MonsterCompendium - popup opened")
    }

    fun trackPopupClosed() {
        analytics.track(
            eventName = "MonsterCompendium - popup closed",
        )
    }

    fun trackAlphabetIndexClicked(position: Int) {
        analytics.track(
            eventName = "MonsterCompendium - alphabet index clicked",
            params = mapOf(
                "position" to position,
                "letter" to monsterCompendium?.alphabet?.get(position).orEmpty(),
            )
        )
    }

    fun trackTableContentIndexClicked(position: Int) {
        analytics.track(
            eventName = "MonsterCompendium - table content index clicked",
            params = mapOf(
                "position" to position,
                "tableContent" to monsterCompendium?.tableContent?.get(position)?.text.orEmpty(),
            )
        )
    }

    fun trackTableContentClosed() {
        analytics.track(
            eventName = "MonsterCompendium - table content closed",
        )
    }

    fun trackErrorButtonClick() {
        analytics.track(
            eventName = "MonsterCompendium - error button click",
        )
    }
}