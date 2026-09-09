package br.alexandregpereira.hunter.revenue

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock

/**
 * Remembers the offer for [ttlInMillis]. Opening the paywall asks for the offer twice, once to
 * decide whether to show it and once to render the price, and the store SDKs do not cache
 * offerings themselves. The mutex also collapses concurrent callers on a cold cache into a single
 * request.
 *
 * Failures are never cached: a transient error would otherwise leave the paywall without a price
 * for the whole TTL.
 */
fun GetCurrentOffer.cached(
    ttlInMillis: Long = DEFAULT_OFFER_CACHE_TTL_IN_MILLIS,
    clock: Clock = Clock.System,
): GetCurrentOffer = CachedGetCurrentOffer(
    origin = this,
    ttlInMillis = ttlInMillis,
    clock = clock,
)

const val DEFAULT_OFFER_CACHE_TTL_IN_MILLIS: Long = 30L * 60 * 1000

private class CachedGetCurrentOffer(
    private val origin: GetCurrentOffer,
    private val ttlInMillis: Long,
    private val clock: Clock,
) : GetCurrentOffer {

    private val mutex = Mutex()
    private var cachedOffer: Offer? = null
    private var cachedAtInMillis: Long = 0L

    override suspend fun invoke(): Offer = mutex.withLock {
        val now = clock.now().toEpochMilliseconds()
        cachedOffer?.let { offer ->
            if (now - cachedAtInMillis < ttlInMillis) {
                return@withLock offer
            }
        }
        val offer = origin()
        cachedOffer = offer
        cachedAtInMillis = now
        offer
    }
}
