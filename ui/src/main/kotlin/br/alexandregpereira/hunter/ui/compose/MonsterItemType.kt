/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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

import androidx.annotation.DrawableRes
import br.alexandregpereira.hunter.ui.R

enum class MonsterItemType(@DrawableRes val iconRes: Int) {
    ABERRATION(R.drawable.ic_aberration),
    BEAST(R.drawable.ic_beast),
    CELESTIAL(R.drawable.ic_celestial),
    CONSTRUCT(R.drawable.ic_construct),
    DRAGON(R.drawable.ic_dragon),
    ELEMENTAL(R.drawable.ic_elemental),
    FEY(R.drawable.ic_fey),
    FIEND(R.drawable.ic_fiend),
    GIANT(R.drawable.ic_giant),
    HUMANOID(R.drawable.ic_humanoid),
    MONSTROSITY(R.drawable.ic_monstrosity),
    OOZE(R.drawable.ic_ooze),
    PLANT(R.drawable.ic_plant),
    UNDEAD(R.drawable.ic_undead)
}
