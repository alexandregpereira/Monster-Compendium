package br.alexandregpereira.hunter.paywall.domain

import br.alexandregpereira.hunter.revenue.GetPaywallCooldownIntervals
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

private const val DAY = 24L * 60 * 60 * 1000
private val INTERVALS = listOf(3 * DAY, 7 * DAY, 14 * DAY, 30 * DAY)

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaywallCooldownTest {

    private val mapSettings = MapSettings()
    private val clock = FakeClock()
    private var intervalsCallCount = 0

    @Test
    fun `is not in cooldown on a fresh install`() = runTest {
        val cooldown = createCooldown()

        assertFalse(cooldown.isInCooldown())
    }

    @Test
    fun `does not read the intervals when nothing was dismissed yet`() = runTest {
        val cooldown = createCooldown()

        cooldown.isInCooldown()

        assertEquals(0, intervalsCallCount)
    }

    @Test
    fun `the first dismissal stores the count and the current time`() = runTest {
        clock.timeInMillis = 1_000L
        val cooldown = createCooldown()

        cooldown.registerDismissal()

        assertEquals(1, mapSettings.getInt("paywall_dismiss_count", 0))
        assertEquals(1_000L, mapSettings.getLong("paywall_last_dismissed_at", 0))
    }

    @Test
    fun `one dismissal keeps the paywall hidden until the first interval elapses`() = runTest {
        val cooldown = createCooldown()
        cooldown.registerDismissal()

        clock.timeInMillis = 3 * DAY - 1
        assertTrue(cooldown.isInCooldown())

        clock.timeInMillis = 3 * DAY + 1
        assertFalse(cooldown.isInCooldown())
    }

    @Test
    fun `the interval escalates on the second dismissal`() = runTest {
        val cooldown = createCooldown()
        cooldown.registerDismissal()
        clock.timeInMillis = 3 * DAY + 1
        cooldown.registerDismissal()

        clock.timeInMillis += 3 * DAY + 1
        assertTrue(cooldown.isInCooldown())

        clock.timeInMillis += 4 * DAY
        assertFalse(cooldown.isInCooldown())
    }

    @Test
    fun `the interval caps at the last one instead of going out of bounds`() = runTest {
        val cooldown = createCooldown()
        repeat(times = 5) { cooldown.registerDismissal() }

        clock.timeInMillis = 30 * DAY - 1
        assertTrue(cooldown.isInCooldown())

        clock.timeInMillis = 30 * DAY + 1
        assertFalse(cooldown.isInCooldown())
    }

    @Test
    fun `reset clears the ladder so the next dismissal starts over`() = runTest {
        val cooldown = createCooldown()
        repeat(times = 3) { cooldown.registerDismissal() }

        cooldown.reset()

        assertFalse(cooldown.isInCooldown())
        assertNull(mapSettings.getLongOrNull("paywall_last_dismissed_at"))
        cooldown.registerDismissal()
        assertEquals(1, mapSettings.getInt("paywall_dismiss_count", 0))
    }

    @Test
    fun `empty intervals never hide the paywall`() = runTest {
        val cooldown = createCooldown(intervals = emptyList())
        cooldown.registerDismissal()

        assertFalse(cooldown.isInCooldown())
    }

    @Test
    fun `a clock moved backwards never locks the user out`() = runTest {
        clock.timeInMillis = 10 * DAY
        val cooldown = createCooldown()
        cooldown.registerDismissal()

        clock.timeInMillis = 5 * DAY

        assertFalse(cooldown.isInCooldown())
    }

    @Test
    fun `the legacy permanent flag is removed and no longer blocks the paywall`() = runTest {
        mapSettings.putBoolean("paywall_was_closed", true)
        val cooldown = createCooldown()

        assertFalse(cooldown.isInCooldown())
        assertFalse(mapSettings.hasKey("paywall_was_closed"))
    }

    private fun createCooldown(
        intervals: List<Long> = INTERVALS,
    ): PaywallCooldown = PaywallCooldown(
        settings = PaywallSettings(
            settings = mapSettings,
            dispatcher = Dispatchers.Unconfined,
        ),
        getPaywallCooldownIntervals = GetPaywallCooldownIntervals {
            intervalsCallCount++
            intervals
        },
        clock = clock,
    )
}

private class FakeClock(var timeInMillis: Long = 0L) : Clock {
    override fun now(): Instant = Instant.fromEpochMilliseconds(timeInMillis)
}
