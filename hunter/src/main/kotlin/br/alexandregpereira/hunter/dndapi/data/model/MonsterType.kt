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

package br.alexandregpereira.hunter.dndapi.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class MonsterType {
    @SerialName("aberration")
    ABERRATION,
    @SerialName("beast")
    BEAST,
    @SerialName("celestial")
    CELESTIAL,
    @SerialName("construct")
    CONSTRUCT,
    @SerialName("dragon")
    DRAGON,
    @SerialName("elemental")
    ELEMENTAL,
    @SerialName("fey")
    FEY,
    @SerialName("fiend")
    FIEND,
    @SerialName("giant")
    GIANT,
    @SerialName("humanoid")
    HUMANOID,
    @SerialName("monstrosity")
    MONSTROSITY,
    @SerialName("ooze")
    OOZE,
    @SerialName("plant")
    PLANT,
    @SerialName("undead")
    UNDEAD,
    @SerialName("swarm of Tiny beasts")
    OTHER
}