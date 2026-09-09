package br.alexandregpereira.hunter.paywall.domain

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class PaywallSettings(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val dismissCountKey = "paywall_dismiss_count"
    private val lastDismissedAtKey = "paywall_last_dismissed_at"

    /**
     * Written by a previous version that blocked the paywall forever after the first dismissal.
     * It is only removed now, so the users it locked out re-enter the cooldown ladder.
     */
    private val legacyPaywallWasClosedKey = "paywall_was_closed"

    suspend fun getDismissCount(): Int = withContext(dispatcher) {
        settings.getInt(dismissCountKey, defaultValue = 0)
    }

    suspend fun getLastDismissedAtInMillis(): Long? = withContext(dispatcher) {
        settings.getLongOrNull(lastDismissedAtKey)
    }

    suspend fun saveDismissal(count: Int, atInMillis: Long) = withContext(dispatcher) {
        settings.putInt(dismissCountKey, count)
        settings.putLong(lastDismissedAtKey, atInMillis)
    }

    suspend fun clearDismissals() = withContext(dispatcher) {
        settings.remove(dismissCountKey)
        settings.remove(lastDismissedAtKey)
        settings.remove(legacyPaywallWasClosedKey)
    }

    suspend fun removeLegacyPaywallWasClosedFlag() = withContext(dispatcher) {
        if (settings.hasKey(legacyPaywallWasClosedKey)) {
            settings.remove(legacyPaywallWasClosedKey)
        }
    }
}
