package br.alexandregpereira.hunter.revenue.domain

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class PaywallSettings(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val paywallWasClosedKey = "paywall_was_closed"

    suspend fun savePaywallWasClosedFlag(paywallWasClosed: Boolean) = withContext(dispatcher) {
        settings.putBoolean(paywallWasClosedKey, paywallWasClosed)
    }

    suspend fun getPaywallWasClosedFlag(): Boolean = withContext(dispatcher) {
        settings.getBoolean(paywallWasClosedKey, false)
    }
}
