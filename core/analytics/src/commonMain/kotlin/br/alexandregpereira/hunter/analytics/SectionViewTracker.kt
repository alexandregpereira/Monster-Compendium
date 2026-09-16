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

package br.alexandregpereira.hunter.analytics

/**
 * Reports each section of a screen only the first time it appears in a visit, so scrolling back and
 * forth over the same sections does not send the same event again. The number of events a visit can
 * send is therefore capped at the number of sections the screen has.
 *
 * Call [reset] when a visit starts.
 */
class SectionViewTracker {

    private val viewed = mutableSetOf<String>()

    /**
     * @return true the first time [sectionId] is seen in this visit, and false every time after.
     */
    fun isFirstView(sectionId: String): Boolean = viewed.add(sectionId)

    fun reset() {
        viewed.clear()
    }
}
