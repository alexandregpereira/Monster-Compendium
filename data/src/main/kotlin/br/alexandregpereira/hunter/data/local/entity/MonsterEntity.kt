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

package br.alexandregpereira.hunter.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monster")
internal data class MonsterEntity(
    @PrimaryKey val index: String,
    val type: String,
    val subtype: String?,
    val group: String?,
    val challengeRating: Float,
    val name: String,
    val subtitle: String,
    val imageUrl: String,
    val backgroundColorLight: String,
    val backgroundColorDark: String,
    val isHorizontalImage: Boolean,
    val size: String,
    val alignment: String,
    val armorClass: Int,
    val hitPoints: Int,
    val hitDice: String,
    @Embedded val speedEntity: SpeedEntity,
    val abilityScores: String,
    val savingThrows: String,
    val skills: String,
    val damageVulnerabilities: String,
    val damageResistances: String,
    val damageImmunities: String,
    val conditionImmunities: String,
    val senses: String,
    val languages: String,
    val specialAbilities: String,
    val actions: String,
    val sourceName: String,
    val sourceAcronym: String
)
