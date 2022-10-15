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

package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class SpeedEntity(
    @PrimaryKey
    val id: String,
    val hover: Boolean,
    val monsterIndex: String
)

data class SpeedWithValuesEntity(
    @Embedded val speed: SpeedEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "speedId",
    )
    val values: List<SpeedValueEntity>,
)

@Entity(primaryKeys = ["type", "speedId"])
data class SpeedValueEntity(
    val type: String,
    val valueFormatted: String,
    val speedId: String
)
