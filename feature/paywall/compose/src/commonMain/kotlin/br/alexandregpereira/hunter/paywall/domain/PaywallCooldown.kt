package br.alexandregpereira.hunter.paywall.domain

import br.alexandregpereira.hunter.revenue.GetPaywallCooldownIntervals
import kotlin.time.Clock

/**
 * Decides how long the automatic paywall stays hidden after the user dismisses it. The interval
 * escalates with the number of dismissals and stops growing at the last configured interval.
 */
internal class PaywallCooldown(
    private val settings: PaywallSettings,
    private val getPaywallCooldownIntervals: GetPaywallCooldownIntervals,
    private val clock: Clock = Clock.System,
) {

    suspend fun isInCooldown(): Boolean {
        val dismissCount = settings.getDismissCount()
        if (dismissCount <= 0) {
            // Nothing was dismissed yet, so the intervals are not worth a remote config read.
            settings.removeLegacyPaywallWasClosedFlag()
            return false
        }
        val lastDismissedAtInMillis = settings.getLastDismissedAtInMillis() ?: return false
        val intervals = getPaywallCooldownIntervals().takeIf { it.isNotEmpty() } ?: return false
        val interval = intervals[minOf(dismissCount - 1, intervals.lastIndex)]
        val elapsed = clock.now().toEpochMilliseconds() - lastDismissedAtInMillis
        // A clock moved backwards must never lock the user out permanently.
        if (elapsed < 0) return false
        return elapsed < interval
    }

    suspend fun registerDismissal() {
        settings.saveDismissal(
            count = settings.getDismissCount() + 1,
            atInMillis = clock.now().toEpochMilliseconds(),
        )
    }

    suspend fun reset() = settings.clearDismissals()
}
