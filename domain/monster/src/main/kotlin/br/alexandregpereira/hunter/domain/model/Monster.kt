/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.model

import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting

data class Monster(
    val preview: MonsterPreview,
    val subtype: String? = null,
    val group: String? = null,
    val subtitle: String = "",
    val size: String = "",
    val alignment: String = "",
    val stats: Stats = Stats(),
    val senses: List<String> = emptyList(),
    val languages: String = "",
    val sourceName: String = "",
    val speed: Speed = Speed(hover = false, values = emptyList()),
    val abilityScores: List<AbilityScore> = emptyList(),
    val savingThrows: List<Proficiency> = emptyList(),
    val skills: List<Proficiency> = emptyList(),
    val damageVulnerabilities: List<Damage> = emptyList(),
    val damageResistances: List<Damage> = emptyList(),
    val damageImmunities: List<Damage> = emptyList(),
    val conditionImmunities: List<Condition> = emptyList(),
    val specialAbilities: List<AbilityDescription> = emptyList(),
    val actions: List<Action> = emptyList(),
    val legendaryActions: List<Action> = emptyList(),
    val reactions: List<AbilityDescription> = emptyList(),
    val spellcastings: List<Spellcasting> = emptyList(),
) {
    val index: String
        get() = preview.index
    val name: String
        get() = preview.name
    val type: MonsterType
        get() = preview.type
    val challengeRating: Float
        get() = preview.challengeRating
    val imageData: MonsterImageData
        get() = preview.imageData
}

data class MonsterPreview(
    val index: String,
    val name: String = "",
    val type: MonsterType = MonsterType.ABERRATION,
    val challengeRating: Float = 0f,
    val imageData: MonsterImageData = MonsterImageData()
)

data class MonsterImageData(
    val url: String = "",
    val backgroundColor: Color = Color(),
    val isHorizontal: Boolean = false
)

data class Color(
    val light: String = "",
    val dark: String = ""
)
