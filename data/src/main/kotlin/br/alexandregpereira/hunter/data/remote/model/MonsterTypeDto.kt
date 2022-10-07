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

package br.alexandregpereira.hunter.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class MonsterTypeDto {
    @SerialName("ABERRATION")
    ABERRATION,
    @SerialName("BEAST")
    BEAST,
    @SerialName("CELESTIAL")
    CELESTIAL,
    @SerialName("CONSTRUCT")
    CONSTRUCT,
    @SerialName("DRAGON")
    DRAGON,
    @SerialName("ELEMENTAL")
    ELEMENTAL,
    @SerialName("FEY")
    FEY,
    @SerialName("FIEND")
    FIEND,
    @SerialName("GIANT")
    GIANT,
    @SerialName("HUMANOID")
    HUMANOID,
    @SerialName("MONSTROSITY")
    MONSTROSITY,
    @SerialName("OOZE")
    OOZE,
    @SerialName("PLANT")
    PLANT,
    @SerialName("UNDEAD")
    UNDEAD
}