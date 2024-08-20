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

package br.alexandregpereira.hunter.ui.compendium.monster

import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_aberration
import br.alexandregpereira.hunter.ui.resources.ic_beast
import br.alexandregpereira.hunter.ui.resources.ic_celestial
import br.alexandregpereira.hunter.ui.resources.ic_construct
import br.alexandregpereira.hunter.ui.resources.ic_dragon
import br.alexandregpereira.hunter.ui.resources.ic_elemental
import br.alexandregpereira.hunter.ui.resources.ic_fey
import br.alexandregpereira.hunter.ui.resources.ic_fiend
import br.alexandregpereira.hunter.ui.resources.ic_giant
import br.alexandregpereira.hunter.ui.resources.ic_humanoid
import br.alexandregpereira.hunter.ui.resources.ic_monstrosity
import br.alexandregpereira.hunter.ui.resources.ic_ooze
import br.alexandregpereira.hunter.ui.resources.ic_plant
import br.alexandregpereira.hunter.ui.resources.ic_undead
import org.jetbrains.compose.resources.DrawableResource

data class MonsterCardState(
    val index: String,
    val name: String,
    val imageState: MonsterImageState,
)

data class MonsterImageState(
    val url: String,
    val type: MonsterTypeState,
    val backgroundColor: ColorState,
    val challengeRating: String,
    val contentScale: AppImageContentScale,
    val isHorizontal: Boolean = false,
    val contentDescription: String = "",
)

data class ColorState(
    val light: String,
    val dark: String
) {

    fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}

enum class MonsterTypeState(val icon: DrawableResource) {
    ABERRATION(Res.drawable.ic_aberration),
    BEAST(Res.drawable.ic_beast),
    CELESTIAL(Res.drawable.ic_celestial),
    CONSTRUCT(Res.drawable.ic_construct),
    DRAGON(Res.drawable.ic_dragon),
    ELEMENTAL(Res.drawable.ic_elemental),
    FEY(Res.drawable.ic_fey),
    FIEND(Res.drawable.ic_fiend),
    GIANT(Res.drawable.ic_giant),
    HUMANOID(Res.drawable.ic_humanoid),
    MONSTROSITY(Res.drawable.ic_monstrosity),
    OOZE(Res.drawable.ic_ooze),
    PLANT(Res.drawable.ic_plant),
    UNDEAD(Res.drawable.ic_undead)
}
