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

package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.uuid.generateUUID

data class MonsterRegistrationState(
    val isLoading: Boolean = true,
    val isOpen: Boolean = false,
    val monster: MonsterState = MonsterState(),
    val initialSelectedStepIndex: Int = 0,
    val isSaveButtonEnabled: Boolean = false,
    val strings: MonsterRegistrationStrings = MonsterRegistrationStrings(),
    val tableContent: Map<String, String> = emptyMap(),
    val isTableContentOpen: Boolean = false,
)

data class MonsterState(
    val index: String = "",
    val info: MonsterInfoState = MonsterInfoState(),
    val stats: StatsState = StatsState(),
    val speedValues: List<SpeedValueState> = emptyList(),
    val abilityScores: List<AbilityScoreState> = emptyList(),
    val savingThrows: List<SavingThrowState> = emptyList(),
    val skills: List<SkillState> = emptyList(),
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
    val loreEntries: List<MonsterLoreEntryState> = emptyList(),
    internal val keysList: List<String> = emptyList(),
) {
    val keys: Iterator<String> = KeyIterator(keysList)
}

private class KeyIterator(
    private val keysList: List<String> = emptyList(),
) : Iterator<String> {
    private var keys: Iterator<String> = keysList.iterator()

    override fun hasNext(): Boolean = keys.hasNext()

    override fun next(): String {
        if (!hasNext()) {
            keys = keysList.iterator()
        }

        return keys.next()
    }
}

data class MonsterInfoState(
    val name: String = "",
    val subtitle: String = "",
    val group: String = "",
    val imageUrl: String= "",
    val backgroundColorLight: String = "",
    val backgroundColorDark: String = "",
    val isImageHorizontal: Boolean = false,
    val typeIndex: Int = 0,
    val typeOptions: List<String> = emptyList(),
    val challengeRating: String = "",
    val imageContentScale: MonsterImageContentScale = MonsterImageContentScale.Fit,
) {
    val type: String = typeOptions.getOrNull(typeIndex).orEmpty()
}

data class StatsState(
    val armorClass: Int = 0,
    val hitPoints: Int = 0,
    val hitDice: String = "",
)

data class SpeedValueState(
    val key: String = generateUUID(),
    val typeIndex: Int = 0,
    val value: String = "",
    val options: List<String> = emptyList(),
) {

    val type: String = options.getOrNull(typeIndex).orEmpty()
}

data class AbilityScoreState(
    val key: String = generateUUID(),
    val value: Int = 0,
    val name: String = "",
)

data class SavingThrowState(
    val key: String = generateUUID(),
    val modifier: Int = 0,
    val selectedIndex: Int = 0,
    val typeOptions: List<TypeState> = emptyList(),
) {

    private val filteredTypeOptions: List<TypeState> = typeOptions.filter { it.enabled }
    val filteredOptions: List<String> = filteredTypeOptions.map { it.name }
    val name: String = typeOptions.getOrNull(selectedIndex)?.name.orEmpty()

    fun selectedIndex(filteredIndex: Int): Int {
        return filteredTypeOptions[filteredIndex].index
    }
}

data class SkillState(
    val key: String = generateUUID(),
    val modifier: Int = 0,
    val name: String = "",
)

data class ConditionState(
    val key: String = generateUUID(),
    val typeOptions: List<TypeState> = emptyList(),
    val selectedIndex: Int = 0,
    val name: String = typeOptions.getOrNull(selectedIndex)?.name.orEmpty(),
) {
    private val filteredTypeOptions: List<TypeState> = typeOptions.filter { it.enabled }
    val filteredOptions: List<String> = filteredTypeOptions.map { it.name }
    val typeName: String = typeOptions.getOrNull(selectedIndex)?.name.orEmpty()

    fun selectedIndex(filteredIndex: Int): Int {
        return filteredTypeOptions[filteredIndex].index
    }
}

data class AbilityDescriptionState(
    val key: String = generateUUID(),
    val name: String = "",
    val description: String = "",
)

data class ActionState(
    val key: String = generateUUID(),
    val damageDices: List<DamageDiceState> = emptyList(),
    val attackBonus: Int? = null,
    val abilityDescription: AbilityDescriptionState = AbilityDescriptionState(),
)

data class DamageState(
    val key: String = generateUUID(),
    val selectedIndex: Int = 0,
    val typeOptions: List<TypeState> = emptyList(),
    val otherName: String? = null,
) {

    private val filteredTypeOptions: List<TypeState> = typeOptions.filter { it.enabled }
    val filteredOptions: List<String> = filteredTypeOptions.map { it.name }
    val typeName: String = typeOptions.getOrNull(selectedIndex)?.name.orEmpty()

    fun selectedIndex(filteredIndex: Int): Int {
        return filteredTypeOptions[filteredIndex].index
    }
}

data class TypeState(
    val index: Int = 0,
    val name: String = "",
    val enabled: Boolean = true,
)

data class DamageDiceState(
    val key: String = generateUUID(),
    val dice: String = "",
    val damage: DamageState = DamageState(),
) {
    val name: String = damage.typeName
}

data class SpellcastingState(
    val key: String = generateUUID(),
    val description: String = "",
    val spellsByGroup: List<SpellsByGroupState> = emptyList(),
    val options: List<String> = emptyList(),
    val selectedIndex: Int = 0,
) {

    val name: String = options.getOrNull(selectedIndex).orEmpty()
}

data class SpellsByGroupState(
    val key: String = generateUUID(),
    val group: String = "",
    val spells: List<SpellPreviewState> = emptyList(),
)

data class SpellPreviewState(
    val index: String = "",
    val name: String = "",
)

data class MonsterLoreEntryState(
    val key: String = generateUUID(),
    val title: String = "",
    val description: String = "",
)

internal enum class SectionTitle {
    Image,
    Header,
    Stats,
    Speed,
    AbilityScores,
    SavingThrows,
    Skills,
    DamageVulnerabilities,
    DamageResistances,
    DamageImmunities,
    ConditionImmunities,
    Senses,
    Languages,
    SpecialAbilities,
    Actions,
    Reactions,
    LegendaryActions,
    Spellcastings,
    MonsterLore,
}
