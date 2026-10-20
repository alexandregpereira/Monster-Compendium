/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import org.junit.Test
import java.util.Locale
import kotlin.test.assertEquals

class MonsterXpFormattedTest {

    @Test
    fun `xp below one thousand is shown in full`() {
        assertEquals("100 XP", xpFormatted(challengeRating = 0.5f))
        assertEquals("700 XP", xpFormatted(challengeRating = 3f))
    }

    @Test
    fun `round thousands have no decimal`() {
        assertEquals("5k XP", xpFormatted(challengeRating = 9f))
        assertEquals("10k XP", xpFormatted(challengeRating = 13f))
        assertEquals("155k XP", xpFormatted(challengeRating = 30f))
    }

    @Test
    fun `hundreds are shown as one decimal`() {
        assertEquals("1.1k XP", xpFormatted(challengeRating = 4f))
        assertEquals("5.9k XP", xpFormatted(challengeRating = 10f))
        assertEquals("11.5k XP", xpFormatted(challengeRating = 14f))
    }

    @Test
    fun `format does not depend on the device locale`() {
        val defaultLocale = Locale.getDefault()
        try {
            Locale.setDefault(Locale.FRANCE)
            assertEquals("5.9k XP", xpFormatted(challengeRating = 10f))
        } finally {
            Locale.setDefault(defaultLocale)
        }
    }

    private fun xpFormatted(challengeRating: Float): String {
        return MonsterFactory.createEmpty("goblin").copy(
            challengeRatingData = ChallengeRating.create(challengeRating),
        ).xpFormatted()
    }
}
