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

package br.alexandregpereira.hunter.detail.ui

import androidx.annotation.DrawableRes
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState

data class MonsterState(
    val index: String,
    val name: String,
    val imageState: MonsterImageState,
    val subtype: String?,
    val group: String?,
    val subtitle: String,
    val size: String,
    val alignment: String,
    val stats: StatsState,
    val speed: SpeedState,
    val abilityScores: List<AbilityScoreState> = emptyList(),
    val savingThrows: List<ProficiencyState> = emptyList(),
    val skills: List<ProficiencyState> = emptyList(),
    val damageVulnerabilities: List<DamageState> = emptyList(),
    val damageResistances: List<DamageState> = emptyList(),
    val damageImmunities: List<DamageState> = emptyList(),
    val conditionImmunities: List<ConditionState> = emptyList(),
    val senses: List<String>,
    val languages: String,
    val specialAbilities: List<AbilityDescriptionState>,
    val actions: List<ActionState>,
    val reactions: List<AbilityDescriptionState>,
    val spellcastings: List<SpellcastingState> = emptyList(),
)

data class StatsState(
    val armorClass: Int,
    val hitPoints: Int,
    val hitDice: String,
)

data class SpeedState(
    val hover: Boolean,
    val values: List<SpeedValueState>,
)

data class SpeedValueState(
    val type: SpeedTypeState,
    val valueFormatted: String
)

enum class SpeedTypeState(@DrawableRes val iconRes: Int) {
    BURROW(R.drawable.ic_ghost),
    CLIMB(R.drawable.ic_climbing),
    FLY(R.drawable.ic_superhero),
    WALK(R.drawable.ic_runer_silhouette_running_fast),
    SWIM(R.drawable.ic_swimmer)
}

data class AbilityScoreState(
    val name: String,
    val value: Int,
    val modifier: Int
)

data class ProficiencyState(
    val index: String,
    val modifier: Int,
    val name: String
)

data class DamageState(
    val index: String,
    val type: DamageTypeState,
    val name: String
)

enum class DamageTypeState(@DrawableRes val iconRes: Int? = null) {
    ACID(R.drawable.ic_acid),
    BLUDGEONING(R.drawable.ic_bludgeoning),
    COLD(R.drawable.ic_cold),
    FIRE(R.drawable.ic_elemental),
    LIGHTNING(R.drawable.ic_lightning),
    NECROTIC(R.drawable.ic_undead),
    PIERCING(R.drawable.ic_piercing),
    POISON(R.drawable.ic_poison),
    PSYCHIC(R.drawable.ic_psychic),
    RADIANT(R.drawable.ic_radiant),
    SLASHING(R.drawable.ic_slashing),
    THUNDER(R.drawable.ic_thunder),
    OTHER,
}

data class ConditionState(
    val index: String,
    val type: ConditionTypeState,
    val name: String
)

enum class ConditionTypeState(@DrawableRes val iconRes: Int) {
    BLINDED(R.drawable.ic_blinded),
    CHARMED(R.drawable.ic_charmed),
    DEAFENED(R.drawable.ic_deafened),
    EXHAUSTION(R.drawable.ic_exhausted),
    FRIGHTENED(R.drawable.ic_frightened),
    GRAPPLED(R.drawable.ic_grappled),
    PARALYZED(R.drawable.ic_paralyzed),
    PETRIFIED(R.drawable.ic_petrified),
    POISONED(R.drawable.ic_poison),
    PRONE(R.drawable.ic_prone),
    RESTRAINED(R.drawable.ic_restrained),
    STUNNED(R.drawable.ic_stuned),
    UNCONSCIOUS(R.drawable.ic_unconscious),
}

data class AbilityDescriptionState(
    val name: String,
    val description: String
)

data class ActionState(
    val damageDices: List<DamageDiceState>,
    val attackBonus: Int?,
    val abilityDescription: AbilityDescriptionState
)

data class DamageDiceState(
    val dice: String,
    val damage: DamageState
)

data class MonsterImageState(
    val url: String,
    val type: MonsterTypeState,
    val backgroundColor: ColorState,
    val challengeRating: Float,
    val isHorizontal: Boolean = false,
    val contentDescription: String = ""
)

data class ColorState(
    val light: String,
    val dark: String
) {

    fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}

enum class MonsterTypeState(@DrawableRes val iconRes: Int) {
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

data class SpellcastingState(
    val type: SpellcastingTypeState,
    val description: String,
    val spellsByGroup: Map<String, List<SpellPreviewState>>
)

data class SpellPreviewState(
    val index: String,
    val name: String,
    val school: SchoolOfMagicState
)

enum class SpellcastingTypeState(val nameRes: Int) {
    SPELLCASTER(R.string.monster_detail_spellcasting),
    INNATE(R.string.monster_detail_innate_spellcasting)
}
