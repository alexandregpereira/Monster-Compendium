package br.alexandregpereira.hunter.revenue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Clock
import kotlin.time.Instant

private const val TTL = 30L * 60 * 1000
private val OFFER = Offer(id = "offer-id", value = "R$ 9,90", period = OfferPeriod.MONTHLY)

@OptIn(ExperimentalCoroutinesApi::class)
internal class CachedGetCurrentOfferTest {

    private val clock = FakeClock()
    private var callCount = 0

    @Test
    fun `a second call within the ttl does not reach the origin`() = runTest {
        val getCurrentOffer = createCached()

        assertEquals(OFFER, getCurrentOffer())
        clock.timeInMillis = TTL - 1
        assertEquals(OFFER, getCurrentOffer())

        assertEquals(1, callCount)
    }

    @Test
    fun `a call after the ttl fetches again`() = runTest {
        val getCurrentOffer = createCached()

        getCurrentOffer()
        clock.timeInMillis = TTL + 1
        getCurrentOffer()

        assertEquals(2, callCount)
    }

    @Test
    fun `a failure is not cached so the next call can succeed`() = runTest {
        var shouldFail = true
        val getCurrentOffer = GetCurrentOffer {
            callCount++
            if (shouldFail) throw GetCurrentOfferException.OfferNotFound("no offer")
            OFFER
        }.cached(ttlInMillis = TTL, clock = clock)

        assertFailsWith<GetCurrentOfferException.OfferNotFound> { getCurrentOffer() }
        shouldFail = false

        assertEquals(OFFER, getCurrentOffer())
        assertEquals(2, callCount)
    }

    @Test
    fun `concurrent callers on a cold cache produce a single request`() = runTest {
        val getCurrentOffer = createCached()

        val results = List(size = 5) { async { getCurrentOffer() } }.awaitAll()

        assertEquals(List(size = 5) { OFFER }, results)
        assertEquals(1, callCount)
    }

    private fun createCached(): GetCurrentOffer = GetCurrentOffer {
        callCount++
        OFFER
    }.cached(ttlInMillis = TTL, clock = clock)
}

private class FakeClock(var timeInMillis: Long = 0L) : Clock {
    override fun now(): Instant = Instant.fromEpochMilliseconds(timeInMillis)
}
