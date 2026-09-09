package br.alexandregpereira.hunter.paywall

import br.alexandregpereira.hunter.ads.consent.AdsConsentManager
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.network.NetworkManager
import br.alexandregpereira.hunter.paywall.domain.PaywallCooldown
import br.alexandregpereira.hunter.paywall.domain.PaywallSettings
import br.alexandregpereira.hunter.paywall.domain.ShouldShowPaywall
import br.alexandregpereira.hunter.paywall.event.PaywallEvent
import br.alexandregpereira.hunter.paywall.event.PaywallEventDispatcher
import br.alexandregpereira.hunter.paywall.event.PaywallResultDispatcher
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.GetPaywallCooldownIntervals
import br.alexandregpereira.hunter.revenue.IsPremium
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached
import br.alexandregpereira.hunter.revenue.Offer
import br.alexandregpereira.hunter.revenue.OfferPeriod
import br.alexandregpereira.hunter.revenue.Purchase
import br.alexandregpereira.hunter.revenue.RestorePurchase
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val DAY = 24L * 60 * 60 * 1000
private const val DISMISS_COUNT_KEY = "paywall_dismiss_count"
private const val LAST_DISMISSED_AT_KEY = "paywall_last_dismissed_at"

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaywallStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val paywallEventDispatcher = PaywallEventDispatcher()
    private val paywallResultDispatcher = PaywallResultDispatcher()
    private val mapSettings = MapSettings()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `closing an automatically opened paywall registers a dismissal`() = runTest {
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        advanceUntilIdle()
        assertTrue(stateHolder.state.value.isOpen)

        stateHolder.onClose()
        advanceUntilIdle()

        assertEquals(1, mapSettings.getInt(DISMISS_COUNT_KEY, 0))
    }

    @Test
    fun `closing a manually opened paywall does not register a dismissal`() = runTest {
        val stateHolder = createStateHolder(shouldShow = false)
        advanceUntilIdle()

        paywallEventDispatcher.dispatchEvent(PaywallEvent.ShowPaywall)
        advanceUntilIdle()
        assertTrue(stateHolder.state.value.isOpen)

        stateHolder.onClose()
        advanceUntilIdle()

        assertEquals(0, mapSettings.getInt(DISMISS_COUNT_KEY, 0))
        assertFalse(mapSettings.hasKey(LAST_DISMISSED_AT_KEY))
    }

    @Test
    fun `closing after a purchase error does not register a dismissal`() = runTest {
        val stateHolder = createStateHolder(purchaseFails = true)

        stateHolder.onStart()
        advanceUntilIdle()
        stateHolder.onSubscribe()
        advanceUntilIdle()
        assertEquals(
            PaywallActionResultState.PurchaseError,
            stateHolder.state.value.actionResultState,
        )

        stateHolder.onClose()
        advanceUntilIdle()

        assertEquals(0, mapSettings.getInt(DISMISS_COUNT_KEY, 0))
    }

    @Test
    fun `closing after an offer error registers a dismissal`() = runTest {
        val stateHolder = createStateHolder(offerFails = true)

        stateHolder.onStart()
        advanceUntilIdle()
        assertEquals(
            PaywallActionResultState.GetCurrentOfferError,
            stateHolder.state.value.actionResultState,
        )

        stateHolder.onClose()
        advanceUntilIdle()

        assertEquals(1, mapSettings.getInt(DISMISS_COUNT_KEY, 0))
    }

    @Test
    fun `a successful subscription clears the cooldown ladder`() = runTest {
        mapSettings.putInt(DISMISS_COUNT_KEY, 3)
        mapSettings.putLong(LAST_DISMISSED_AT_KEY, DAY)
        val stateHolder = createStateHolder()

        stateHolder.onStart()
        advanceUntilIdle()
        stateHolder.onSubscribe()
        advanceUntilIdle()

        assertEquals(0, mapSettings.getInt(DISMISS_COUNT_KEY, 0))
        assertFalse(mapSettings.hasKey(LAST_DISMISSED_AT_KEY))
    }

    @Test
    fun `onStart does not reopen an already open paywall`() = runTest {
        val stateHolder = createStateHolder(shouldShow = false)
        advanceUntilIdle()

        paywallEventDispatcher.dispatchEvent(PaywallEvent.ShowPaywall)
        advanceUntilIdle()

        stateHolder.onStart()
        advanceUntilIdle()
        stateHolder.onClose()
        advanceUntilIdle()

        // The automatic onStart must not have flipped the open to automatic.
        assertEquals(0, mapSettings.getInt(DISMISS_COUNT_KEY, 0))
    }

    private fun createStateHolder(
        shouldShow: Boolean = true,
        offerFails: Boolean = false,
        purchaseFails: Boolean = false,
    ): PaywallStateHolder {
        val paywallSettings = PaywallSettings(
            settings = mapSettings,
            dispatcher = Dispatchers.Unconfined,
        )
        val cooldown = PaywallCooldown(
            settings = paywallSettings,
            getPaywallCooldownIntervals = GetPaywallCooldownIntervals { listOf(3 * DAY) },
        )
        var offerCallCount = 0
        val getCurrentOffer = GetCurrentOffer {
            offerCallCount++
            // ShouldShowPaywall gates on this too, so only the load inside the paywall fails.
            if (offerFails && offerCallCount > 1) error("offer unavailable")
            Offer(id = "offer-id", value = "R$ 9,90", period = OfferPeriod.MONTHLY)
        }
        return PaywallStateHolder(
            paywallEventListener = paywallEventDispatcher,
            paywallResultDispatcher = paywallResultDispatcher,
            shouldShowPaywall = ShouldShowPaywall(
                isSessionUsageLimitReached = IsSessionUsageLimitReached { shouldShow },
                networkManager = object : NetworkManager {
                    override suspend fun isNetworkAvailable(): Boolean = true
                },
                getCurrentOffer = getCurrentOffer,
                cooldown = cooldown,
                analytics = FakeAnalytics(),
                adsConsentManager = FakeAdsConsentManager(),
            ),
            isPremium = IsPremium { false },
            cooldown = cooldown,
            purchase = Purchase { if (purchaseFails) error("purchase failed") },
            getCurrentOffer = getCurrentOffer,
            restorePurchase = RestorePurchase { },
            appLocalization = object : AppLocalization {
                override fun getLanguage(): Language = Language.ENGLISH
            },
            analytics = FakeAnalytics(),
            dispatcher = testDispatcher,
        )
    }
}

private class FakeAdsConsentManager : AdsConsentManager {
    override val canRequestAds: StateFlow<Boolean> = MutableStateFlow(true)
    override fun initialize() = Unit
    override fun showConsentFormIfRequired() = Unit
    override fun loadConsentInfo() = Unit
}

private class FakeAnalytics : Analytics {
    override fun track(eventName: String, params: Map<String, Any?>) = Unit
    override fun setUserProperty(name: String, value: Any) = Unit
    override fun getDeviceId(): String? = null
    override fun logException(throwable: Throwable) = Unit
}
