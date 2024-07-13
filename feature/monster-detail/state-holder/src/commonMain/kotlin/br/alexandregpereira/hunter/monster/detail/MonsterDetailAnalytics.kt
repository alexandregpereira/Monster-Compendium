package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.event.MonsterEvent

class MonsterDetailAnalytics(
    private val analytics: Analytics
) {

    private var loadTracked = false

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackMonsterDetailLoaded(monsterIndex: String, monsters: List<MonsterState>) {
        if (loadTracked) return
        analytics.track(
            eventName = "MonsterDetail - loaded",
            params = mapOf(
                "monsterIndex" to monsterIndex,
                "monstersSize" to monsters.size,
            )
        )
        loadTracked = true
    }

    fun trackMonsterDetailShown(event: MonsterEvent.OnVisibilityChanges.Show) {
        analytics.track(
            eventName = "MonsterDetail - monster detail shown",
            params = mapOf(
                "monsterIndex" to event.index,
                "monsterIndexes" to event.indexes.toString(),
                "enableMonsterPageChangesEventDispatch" to event.enableMonsterPageChangesEventDispatch,
            )
        )
    }

    fun trackMonsterDetailHidden() {
        analytics.track(
            eventName = "MonsterDetail - monster detail hidden",
        )
    }

    fun trackMonsterPageChanged(monsterIndex: String, scrolled: Boolean) {
        analytics.track(
            eventName = "MonsterDetail - page changed",
            params = mapOf(
                "monsterIndex" to monsterIndex,
                "scrolled" to scrolled,
            )
        )
    }

    fun trackMonsterDetailClosed() {
        analytics.track(
            eventName = "MonsterDetail - closed",
        )
    }

    fun trackMonsterDetailOptionsShown() {
        analytics.track(
            eventName = "MonsterDetail - options shown",
        )
    }

    fun trackMonsterDetailOptionsHidden() {
        analytics.track(
            eventName = "MonsterDetail - options hidden",
        )
    }

    fun trackMonsterDetailOptionClicked(option: MonsterDetailOptionState) {
        analytics.track(
            eventName = "MonsterDetail - option clicked",
            params = mapOf(
                "option" to option.name,
            )
        )
    }

    fun trackMonsterDetailSpellClicked(spellIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - spell clicked",
            params = mapOf(
                "spellIndex" to spellIndex,
            )
        )
    }

    fun trackMonsterDetailLoreClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - lore clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailDeleteClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - delete clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailEditClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - edit clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailDeleteConfirmed(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - delete confirmed",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailDeleteCanceled(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - delete canceled",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailResetToOriginalClicked(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - reset to original clicked",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }

    fun trackMonsterDetailResetConfirmed(monsterIndex: String) {
        analytics.track(
            eventName = "MonsterDetail - reset confirmed",
            params = mapOf(
                "monsterIndex" to monsterIndex,
            )
        )
    }
}