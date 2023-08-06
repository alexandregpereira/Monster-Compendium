package br.alexandregpereira.hunter.sync

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.sync.model.SyncStatus

internal class SyncAnalytics(
    private val analytics: Analytics
) {

    fun logException(throwable: Throwable) {
        analytics.logException(throwable)
    }

    fun trackStartSync() {
        analytics.track(
            eventName = "Sync - start",
        )
    }

    fun trackFinishSync() {
        analytics.track(
            eventName = "Sync - finish",
        )
    }

    fun trackSyncStatus(status: SyncStatus, forceSync: Boolean) {
        analytics.track(
            eventName = "Sync - synced",
            params = mapOf(
                "status" to status.name,
                "forceSync" to forceSync
            )
        )
    }

    fun trackTryAgain() {
        analytics.track(
            eventName = "Sync - try again",
        )
    }
}