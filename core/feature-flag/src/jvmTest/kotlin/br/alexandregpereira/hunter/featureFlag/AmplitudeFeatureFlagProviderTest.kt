package br.alexandregpereira.hunter.featureFlag

import br.alexandregpereira.hunter.analytics.Analytics
import kotlinx.coroutines.test.runTest
import java.net.UnknownHostException
import java.util.Collections
import java.util.concurrent.ExecutionException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AmplitudeFeatureFlagProviderTest {

    @Test
    fun `isFeatureEnabled returns the default value when the fetch fails`() = runTest {
        val analytics = FakeAnalytics()
        val client = FakeClient(fetchError = RuntimeException("Unable to resolve host"))
        val provider = createProvider(client, analytics)
        provider.initialize()

        assertTrue(provider.isFeatureEnabled(feature = FEATURE, defaultValue = true))
        assertFalse(provider.isFeatureEnabled(feature = FEATURE, defaultValue = false))
        assertEquals(0, client.variantCallCount)
        assertTrue(analytics.exceptions.isNotEmpty())
    }

    @Test
    fun `isFeatureEnabled does not report the failure when the device has no network`() = runTest {
        val analytics = FakeAnalytics()
        val client = FakeClient(fetchError = UnknownHostException("api.lab.amplitude.com"))
        val provider = createProvider(client, analytics)
        provider.initialize()

        assertTrue(provider.isFeatureEnabled(feature = FEATURE, defaultValue = true))
        assertTrue(analytics.exceptions.isEmpty())
    }

    @Test
    fun `isFeatureEnabled does not report a network failure wrapped in other exceptions`() = runTest {
        val analytics = FakeAnalytics()
        val client = FakeClient(
            fetchError = ExecutionException(
                ExecutionException(UnknownHostException("api.lab.amplitude.com"))
            ),
        )
        val provider = createProvider(client, analytics)
        provider.initialize()

        assertTrue(provider.isFeatureEnabled(feature = FEATURE, defaultValue = true))
        assertTrue(analytics.exceptions.isEmpty())
    }

    @Test
    fun `isFeatureEnabled returns false when the fetch succeeds and the flag is off`() = runTest {
        val client = FakeClient(variantValue = "off")
        val provider = createProvider(client)
        provider.initialize()

        assertFalse(provider.isFeatureEnabled(feature = FEATURE, defaultValue = true))
    }

    @Test
    fun `isFeatureEnabled returns true when the fetch succeeds and the flag is on`() = runTest {
        val client = FakeClient(variantValue = "on")
        val provider = createProvider(client)
        provider.initialize()

        assertTrue(provider.isFeatureEnabled(feature = FEATURE, defaultValue = false))
    }

    @Test
    fun `isFeatureEnabled retries the fetch after a failure`() = runTest {
        val client = FakeClient(fetchError = RuntimeException("Unable to resolve host"))
        val provider = createProvider(client)
        provider.initialize()

        assertTrue(provider.isFeatureEnabled(feature = FEATURE, defaultValue = true))
        client.fetchError = null
        client.variantValue = "on"

        assertTrue(provider.isFeatureEnabled(feature = FEATURE, defaultValue = false))
    }

    @Test
    fun `isFeatureEnabled fetches only once after a success`() = runTest {
        val client = FakeClient(variantValue = "on")
        val provider = createProvider(client)
        provider.initialize()

        provider.isFeatureEnabled(feature = FEATURE, defaultValue = false)
        provider.isFeatureEnabled(feature = FEATURE, defaultValue = false)

        assertEquals(1, client.fetchCallCount)
    }

    private fun createProvider(
        client: FakeClient,
        analytics: Analytics = FakeAnalytics(),
    ) = AmplitudeFeatureFlagProvider(
        clientFactory = object : AmplitudeFeatureFlagClient.Factory {
            override fun create(apiKey: String): AmplitudeFeatureFlagClient = client
        },
        apiKey = "any-api-key",
        analytics = analytics,
    )

    private class FakeClient(
        @Volatile var fetchError: Throwable? = null,
        @Volatile var variantValue: String = "",
    ) : AmplitudeFeatureFlagClient {

        // The provider fetches from a background scope on initialize, so these are touched
        // from more than one thread.
        private val fetchCalls = AtomicInteger(0)
        private val variantCalls = AtomicInteger(0)

        val fetchCallCount: Int get() = fetchCalls.get()
        val variantCallCount: Int get() = variantCalls.get()

        override suspend fun fetch() {
            fetchCalls.incrementAndGet()
            fetchError?.let { throw it }
        }

        override fun variant(feature: String): AmplitudeVariant {
            variantCalls.incrementAndGet()
            return AmplitudeVariant(value = variantValue)
        }
    }

    private class FakeAnalytics : Analytics {
        val exceptions = Collections.synchronizedList(mutableListOf<Throwable>())

        override fun track(eventName: String, params: Map<String, Any?>) = Unit
        override fun setUserProperty(name: String, value: Any) = Unit
        override fun getDeviceId(): String? = null
        override fun logException(throwable: Throwable) {
            exceptions += throwable
        }
    }

    private companion object {
        const val FEATURE = "alternative-sources-complete"
    }
}
