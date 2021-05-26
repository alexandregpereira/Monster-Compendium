/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    val actions: String
)
