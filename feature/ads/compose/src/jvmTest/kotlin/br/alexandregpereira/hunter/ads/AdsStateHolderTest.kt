package br.alexandregpereira.hunter.ads

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.paywall.event.PaywallEvent
import br.alexandregpereira.hunter.paywall.event.PaywallEventDispatcher
import br.alexandregpereira.hunter.paywall.event.PaywallResult
import br.alexandregpereira.hunter.paywall.event.PaywallResultDispatcher
import br.alexandregpereira.hunter.revenue.IsPremium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class AdsStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val paywallResultDispatcher = PaywallResultDispatcher()
    private val paywallEventDispatcher = PaywallEventDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `promo banner is shown instead of the ad banner when the app starts`() = runTest {
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        runCurrent()

        assertTrue(stateHolder.state.value.isPromoBannerVisible)
        assertFalse(stateHolder.state.value.isAdBannerVisible)
    }

    @Test
    fun `ad banner replaces the promo banner after the promo minimum duration`() = runTest {
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        advanceTimeBy(14_999)

        assertTrue(stateHolder.state.value.isPromoBannerVisible)

        advanceTimeBy(2)

        assertTrue(stateHolder.state.value.isAdBannerVisible)
        assertFalse(stateHolder.state.value.isPromoBannerVisible)
    }

    @Test
    fun `promo banner comes back when the ad fails to load`() = runTest {
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        advanceTimeBy(15_001)
        stateHolder.onAdFailedToLoad()
        runCurrent()

        assertTrue(stateHolder.state.value.isPromoBannerVisible)
        assertFalse(stateHolder.state.value.isAdBannerVisible)
    }

    @Test
    fun `ad is requested again only after the retry duration`() = runTest {
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        advanceTimeBy(15_001)
        stateHolder.onAdFailedToLoad()
        advanceTimeBy(59_999)

        assertTrue(stateHolder.state.value.isPromoBannerVisible)

        advanceTimeBy(2)

        assertTrue(stateHolder.state.value.isAdBannerVisible)
    }

    @Test
    fun `no banner is shown when the user is premium`() = runTest {
        val stateHolder = createStateHolder(isPremium = true)

        stateHolder.onStart()
        advanceUntilIdle()

        assertFalse(stateHolder.state.value.isPromoBannerVisible)
        assertFalse(stateHolder.state.value.isAdBannerVisible)
    }

    @Test
    fun `banners are hidden after a subscription`() = runTest {
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        advanceUntilIdle()
        paywallResultDispatcher.dispatchEvent(PaywallResult.OnSubscribe)
        advanceUntilIdle()

        assertFalse(stateHolder.state.value.isVisible)
        assertFalse(stateHolder.state.value.isPromoBannerVisible)
        assertFalse(stateHolder.state.value.isAdBannerVisible)
    }

    @Test
    fun `promo banner click opens the paywall`() = runTest {
        val stateHolder = createStateHolder()
        var event: PaywallEvent? = null
        val job = launch { event = paywallEventDispatcher.events.first() }
        advanceUntilIdle()

        stateHolder.onPromoBannerClick()
        advanceUntilIdle()

        assertEquals(PaywallEvent.ShowPaywall, event)
        job.cancel()
    }

    private fun createStateHolder(
        isPremium: Boolean = false,
    ): AdsStateHolder = AdsStateHolder(
        isPremium = IsPremium { isPremium },
        paywallResultListener = paywallResultDispatcher,
        paywallEventDispatcher = paywallEventDispatcher,
        appLocalization = object : AppLocalization {
            override fun getLanguage(): Language = Language.ENGLISH
        },
        analytics = FakeAnalytics(),
        dispatcher = testDispatcher,
    )
}

private class FakeAnalytics : Analytics {
    override fun track(eventName: String, params: Map<String, Any?>) = Unit
    override fun setUserProperty(name: String, value: Any) = Unit
    override fun getDeviceId(): String? = null
    override fun logException(throwable: Throwable) = Unit
}
