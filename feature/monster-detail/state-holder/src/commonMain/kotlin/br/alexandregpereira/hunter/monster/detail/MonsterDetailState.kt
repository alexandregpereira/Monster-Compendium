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

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import kotlin.native.ObjCName

data class MonsterDetailState(
    val isLoading: Boolean = true,
    val monsters: List<MonsterState> = emptyList(),
    val showOptions: Boolean = false,
    val options: List<MonsterDetailOptionState> = emptyList(),
    val measurementUnit: MeasurementUnit = MeasurementUnit.FEET,
    val showDetail: Boolean = false,
    val showCloneForm: Boolean = false,
    val monsterCloneName: String = "",
    val showDeleteConfirmation: Boolean = false,
    val showResetConfirmation: Boolean = false,
    val strings: MonsterDetailStrings = MonsterDetailStrings(),
) {

    companion object {
        val Empty: MonsterDetailState = MonsterDetailState()
    }
}

@ObjCName(name = "MonsterState", exact = true)
data class MonsterState(
    val index: String = "",
    val name: String = "",
    val imageState: MonsterImageState = MonsterImageState(),
    val subtitle: String = "",
    val stats: StatsState = StatsState(),
    val speed: SpeedState = SpeedState(),
    val abilityScores: List<AbilityScoreState> = emptyList(),
    val savingThrows: List<ProficiencyState> = emptyList(),
    val skills: List<ProficiencyState> = emptyList(),
    val damageVulnerabilities: List<DamageState> = emptyList(),
    val damageResistances: List<DamageState> = emptyList(),
    val damageImmunities: List<DamageState> = emptyList(),
    val conditionImmunities: List<ConditionState> = emptyList(),
    val senses: List<String> = emptyList(),
    val languages: String = "",
    val specialAbilities: List<AbilityDescriptionState> = emptyList(),
    val actions: List<ActionState> = emptyList(),
    val legendaryActions: List<ActionState> = emptyList(),
    val reactions: List<AbilityDescriptionState> = emptyList(),
    val spellcastings: List<SpellcastingState> = emptyList(),
    val lore: String = "",
) {

    val type: MonsterType
        get() = imageState.type

    val challengeRating: String
        get() = imageState.challengeRating

    val xp: String
        get() = imageState.xp

    val imageUrl: String
        get() = imageState.url

    val imageContentScale: MonsterImageContentScale
        get() = imageState.contentScale

    fun getBackgroundColor(isDarkTheme: Boolean): String {
        return imageState.backgroundColor.getColor(isDarkTheme)
    }
}

internal fun MonsterState.isComplete(): Boolean = abilityScores.isNotEmpty()

@ObjCName(name = "StatsState", exact = true)
data class StatsState(
    val armorClass: Int = 0,
    val hitPoints: Int = 0,
    val hitDice: String = "",
)

@ObjCName(name = "SpeedState", exact = true)
data class SpeedState(
    val hover: Boolean = false,
    val values: List<SpeedValueState> = emptyList(),
)

@ObjCName(name = "SpeedValueState", exact = true)
data class SpeedValueState(
    val type: SpeedType,
    val valueFormatted: String
)

@ObjCName(name = "AbilityScoreState", exact = true)
data class AbilityScoreState(
    val value: Int,
    val modifier: Int,
    val name: String,
) {
    val shortName: String = name.uppercase().substring(0..2)
}

@ObjCName(name = "ProficiencyState", exact = true)
data class ProficiencyState(
    val index: String,
    val modifier: Int,
    val name: String
)

@ObjCName(name = "DamageState", exact = true)
data class DamageState(
    val index: String,
    val type: DamageType,
    val name: String
)

@ObjCName(name = "ConditionState", exact = true)
data class ConditionState(
    val index: String,
    val type: ConditionType,
    val name: String
)

@ObjCName(name = "AbilityDescriptionState", exact = true)
data class AbilityDescriptionState(
    val name: String,
    val description: String
)

@ObjCName(name = "ActionState", exact = true)
data class ActionState(
    val damageDices: List<DamageDiceState>,
    val attackBonus: Int?,
    val abilityDescription: AbilityDescriptionState
)

@ObjCName(name = "DamageDiceState", exact = true)
data class DamageDiceState(
    val dice: String,
    val damage: DamageState
)

data class MonsterImageState(
    val url: String= "",
    val type: MonsterType = MonsterType.ABERRATION,
    val backgroundColor: ColorState = ColorState(),
    val challengeRating: String = "",
    val xp: String = "",
    val contentDescription: String = "",
    val contentScale: MonsterImageContentScale = MonsterImageContentScale.Fit,
)

@ObjCName(name = "ColorState", exact = true)
data class ColorState(
    val light: String = "",
    val dark: String = "",
) {

    internal fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}

@ObjCName(name = "SpellcastingState", exact = true)
data class SpellcastingState(
    val description: String,
    val spellsByGroup: Map<String, List<SpellPreviewState>>,
    val name: String,
)

@ObjCName(name = "SpellPreviewState", exact = true)
data class SpellPreviewState(
    val index: String,
    val name: String,
    val school: SchoolOfMagic
)

val MonsterDetailState.ShowOptions: MonsterDetailState
    get() = this.copy(showOptions = true)

val MonsterDetailState.HideOptions: MonsterDetailState
    get() = this.copy(showOptions = false)

fun MonsterDetailState.complete(
    monsters: List<MonsterState>,
    options: List<MonsterDetailOptionState>
): MonsterDetailState = this.copy(
    isLoading = false,
    monsters = monsters,
    options = options
)

fun MonsterDetailState.showCloneForm(monsterIndex: String): MonsterDetailState = this.copy(
    showCloneForm = true,
    monsterCloneName = monsters.firstOrNull { it.index == monsterIndex }?.name.orEmpty(),
)

fun MonsterDetailState.hideCloneForm(): MonsterDetailState = this.copy(showCloneForm = false)
