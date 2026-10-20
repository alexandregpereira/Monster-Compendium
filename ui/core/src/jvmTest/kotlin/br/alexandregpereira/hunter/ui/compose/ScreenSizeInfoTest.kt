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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenSizeInfoTest {

    @Test
    fun `phone in portrait is portrait`() {
        assertEquals(ScreenSizeType.Portrait, screenSize(width = 400.dp, height = 860.dp).type)
    }

    @Test
    fun `phone in landscape is landscape compact`() {
        assertEquals(ScreenSizeType.LandscapeCompact, screenSize(width = 800.dp, height = 400.dp).type)
    }

    @Test
    fun `wide tablet in portrait is portrait`() {
        assertEquals(ScreenSizeType.Portrait, screenSize(width = 900.dp, height = 1440.dp).type)
    }

    @Test
    fun `wide tablet in landscape is landscape expanded`() {
        assertEquals(ScreenSizeType.LandscapeExpanded, screenSize(width = 1440.dp, height = 900.dp).type)
    }

    private fun screenSize(width: Dp, height: Dp) = ScreenSizeInfo(
        heightInPixels = height.value.toInt(),
        widthInPixels = width.value.toInt(),
        heightInDp = height,
        widthInDp = width,
    )
}
