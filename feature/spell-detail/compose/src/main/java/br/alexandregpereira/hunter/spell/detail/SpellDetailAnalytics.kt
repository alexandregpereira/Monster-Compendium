package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.analytics.Analytics

internal class SpellDetailAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackSpellLoaded(spell: SpellState) {
        analytics.track(
            eventName = "Spell Detail - loaded",
            params = mapOf(
                "index" to spell.index,
                "name" to spell.name
            )
        )
    }

    fun trackSpellShown(index: String) {
        analytics.track(
            eventName = "Spell Detail - shown",
            params = mapOf(
                "index" to index
            )
        )
    }

    fun trackSpellClosed() {
        analytics.track(
            eventName = "Spell Detail - closed",
        )
    }
}