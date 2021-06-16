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

package br.alexandregpereira.hunter.scripts.format

import br.alexandregpereira.hunter.data.remote.model.AbilityScoreDto
import br.alexandregpereira.hunter.data.remote.model.AbilityScoreTypeDto
import br.alexandregpereira.hunter.data.remote.model.ActionDto
import br.alexandregpereira.hunter.data.remote.model.ConditionDto
import br.alexandregpereira.hunter.data.remote.model.ConditionTypeDto
import br.alexandregpereira.hunter.data.remote.model.DamageDiceDto
import br.alexandregpereira.hunter.data.remote.model.DamageDto
import br.alexandregpereira.hunter.data.remote.model.DamageTypeDto
import br.alexandregpereira.hunter.data.remote.model.MeasurementUnitDto
import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.remote.model.MonsterSizeDto
import br.alexandregpereira.hunter.data.remote.model.MonsterTypeDto
import br.alexandregpereira.hunter.data.remote.model.SavingThrowDto
import br.alexandregpereira.hunter.data.remote.model.SkillDto
import br.alexandregpereira.hunter.data.remote.model.SourceDto
import br.alexandregpereira.hunter.data.remote.model.SpecialAbilityDto
import br.alexandregpereira.hunter.data.remote.model.SpeedDto
import br.alexandregpereira.hunter.data.remote.model.SpeedTypeDto
import br.alexandregpereira.hunter.data.remote.model.SpeedValueDto
import br.alexandregpereira.hunter.dndapi.data.model.APIReference
import br.alexandregpereira.hunter.dndapi.data.model.Action
import br.alexandregpereira.hunter.dndapi.data.model.Monster
import br.alexandregpereira.hunter.dndapi.data.model.MonsterType
import br.alexandregpereira.hunter.dndapi.data.model.Proficiency
import br.alexandregpereira.hunter.dndapi.data.model.Senses
import br.alexandregpereira.hunter.dndapi.data.model.SpecialAbility
import br.alexandregpereira.hunter.image.downloadImage
import br.alexandregpereira.hunter.scripts.JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.JSON_FORMATTED_FILE_NAME
import br.alexandregpereira.hunter.scripts.json
import br.alexandregpereira.hunter.scripts.readJsonFile
import br.alexandregpereira.hunter.scripts.saveJsonFile
import br.alexandregpereira.hunter.scripts.start
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.decodeFromString
import java.util.Locale

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    val monsters = json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .asMonstersFormatted()
        .asSequence()
        .asFlow()
        .filterByImages()
        .flatMapMerge {
            it.downloadImage()
        }
        .toList()
        .filterNotNull()
        .sortedBy { it.name }

    println("\n${monsters.size} monsters formatted")
    monsters.forEach { println("id: ${it.index}, name: ${it.name}") }

    saveJsonFile(monsters, JSON_FORMATTED_FILE_NAME, printJson = false)
}

private fun List<Monster>.asMonstersFormatted(): List<MonsterDto> {
    return this.filter { it.type != MonsterType.OTHER }.map {
        MonsterDto(
            index = it.getId(),
            type = MonsterTypeDto.valueOf(it.type.name),
            subtype = it.subtype,
            group = getGroup(it.index, it.subtype),
            challengeRating = it.challengeRating,
            name = it.name,
            imageUrl = getImageUrl(it.index),
            isHorizontalImage = false,
            size = MonsterSizeDto.valueOf(it.size.uppercase(Locale.ROOT)),
            alignment = it.alignment,
            armorClass = it.armorClass,
            hitPoints = it.hitPoints,
            hitDice = it.hitDice,
            speed = it.asSpeedFormatted(),
            abilityScores = it.asAbilityScoresFormatted(),
            savingThrows = it.getSavingThrows(),
            skills = it.getSkills(),
            damageVulnerabilities = it.damageVulnerabilities.getDamages(),
            damageResistances = it.damageResistances.getDamages(),
            damageImmunities = it.damageImmunities.getDamages(),
            conditionImmunities = it.conditionImmunities.asConditionsFormatted(),
            senses = it.senses.asSensesFormatted(),
            languages = it.languages,
            specialAbilities = it.specialAbilities.asSpecialAbilitiesFormatted(),
            actions = it.actions.asActionsFormatted(),
            source = SourceDto(
                name = "System Reference Document",
                acronym = "SRD"
            )
        ).formatSubtitle()
    }
}

private fun Monster.getId(): String {
    return index
}

private fun Monster.asSpeedFormatted(): SpeedDto = speed.run {
    val burrow = createSpeedValue(SpeedTypeDto.BURROW, burrow)
    val climb = createSpeedValue(SpeedTypeDto.CLIMB, climb)
    val fly = createSpeedValue(SpeedTypeDto.FLY, fly)
    val walk = createSpeedValue(SpeedTypeDto.WALK, walk)
    val swim = createSpeedValue(SpeedTypeDto.SWIM, swim)
    return SpeedDto(
        hover = hover,
        values = listOfNotNull(burrow, climb, fly, walk, swim)
    )
}

internal fun createSpeedValue(speedType: SpeedTypeDto, value: String?): SpeedValueDto? = value?.let {
    val distance = it.split(" ").first().toIntOrNull() ?: return null
    SpeedValueDto(
        type = speedType,
        measurementUnit = MeasurementUnitDto.FEET,
        value = distance,
        valueFormatted = "$distance ${MeasurementUnitDto.FEET.value}"
    )
}

private fun Monster.asAbilityScoresFormatted(): List<AbilityScoreDto> {
    val strength = AbilityScoreDto(
        type = AbilityScoreTypeDto.STRENGTH,
        value = strength,
        modifier = calculateAbilityScoreModifier(strength)
    )
    val dexterity = AbilityScoreDto(
        type = AbilityScoreTypeDto.DEXTERITY,
        value = dexterity,
        modifier = calculateAbilityScoreModifier(dexterity)
    )
    val constitution = AbilityScoreDto(
        type = AbilityScoreTypeDto.CONSTITUTION,
        value = constitution,
        modifier = calculateAbilityScoreModifier(constitution)
    )
    val intelligence = AbilityScoreDto(
        type = AbilityScoreTypeDto.INTELLIGENCE,
        value = intelligence,
        modifier = calculateAbilityScoreModifier(intelligence)
    )
    val wisdom = AbilityScoreDto(
        type = AbilityScoreTypeDto.WISDOM,
        value = wisdom,
        modifier = calculateAbilityScoreModifier(wisdom)
    )
    val charisma = AbilityScoreDto(
        type = AbilityScoreTypeDto.CHARISMA,
        value = charisma,
        modifier = calculateAbilityScoreModifier(charisma)
    )
    return listOf(strength, dexterity, constitution, intelligence, wisdom, charisma)
}

private fun Monster.getSavingThrows(): List<SavingThrowDto> {
    return this.proficiencies.filter { it.proficiency.index.startsWith("saving-throw-") }.map {
        SavingThrowDto(
            index = it.proficiency.index,
            type = it.proficiency.asSavingThrowType(),
            modifier = it.value
        )
    }
}

private fun Proficiency.asSavingThrowType(): AbilityScoreTypeDto {
    return when (val index = index.removePrefix("saving-throw-")) {
        "str" -> AbilityScoreTypeDto.STRENGTH
        "dex" -> AbilityScoreTypeDto.DEXTERITY
        "con" -> AbilityScoreTypeDto.CONSTITUTION
        "int" -> AbilityScoreTypeDto.INTELLIGENCE
        "wis" -> AbilityScoreTypeDto.WISDOM
        "cha" -> AbilityScoreTypeDto.CHARISMA
        else -> throw IllegalArgumentException("Invalid Saving throw: $index")
    }
}

private fun Monster.getSkills(): List<SkillDto> {
    return this.proficiencies.filter { it.proficiency.index.startsWith("skill-") }.map {
        SkillDto(
            index = it.proficiency.index,
            modifier = it.value,
            name = it.proficiency.name.removePrefix("Skill: ")
        )
    }
}

private fun List<String>.getDamages(): List<DamageDto> {
    return this.map {
        it.getDamage()
    }
}

private fun String.getDamage(): DamageDto {
    val damageType = when {
        this.startsWith("acid") -> DamageTypeDto.ACID
        this.startsWith("bludgeoning") -> DamageTypeDto.BLUDGEONING
        this.startsWith("cold") -> DamageTypeDto.COLD
        this.startsWith("fire") -> DamageTypeDto.FIRE
        this.startsWith("lightning") -> DamageTypeDto.LIGHTNING
        this.startsWith("necrotic") -> DamageTypeDto.NECROTIC
        this.startsWith("piercing") -> DamageTypeDto.PIERCING
        this.startsWith("poison") -> DamageTypeDto.POISON
        this.startsWith("psychic") -> DamageTypeDto.PSYCHIC
        this.startsWith("radiant") -> DamageTypeDto.RADIANT
        this.startsWith("slashing") -> DamageTypeDto.SLASHING
        this.startsWith("thunder") -> DamageTypeDto.THUNDER
        else -> DamageTypeDto.OTHER
    }
    return DamageDto(
        index = this.lowercase(Locale.ROOT),
        type = damageType,
        name = this.replaceFirstChar { char -> char.titlecase(Locale.ROOT) }
    )
}

private fun List<APIReference>.asConditionsFormatted(): List<ConditionDto> {
    return this.mapNotNull {
        runCatching {
            ConditionTypeDto.valueOf(it.name.uppercase(Locale.ROOT))
        }.getOrNull()?.let { conditionType ->
            ConditionDto(
                index = it.index,
                type = conditionType,
                name = it.name
            )
        }
    }
}

private fun Senses.asSensesFormatted(): List<String> {
    return listOfNotNull(
        blindsight?.let { "blindsight $it" },
        darkvision?.let { "darkvision $it" },
        truesight?.let { "truesight $it" },
        tremorsense?.let { "tremorsense $it" },
        passivePerception?.let { "passive Perception $it" },
    )
}

private fun List<SpecialAbility>.asSpecialAbilitiesFormatted(): List<SpecialAbilityDto> {
    return this.map {
        SpecialAbilityDto(name = it.name, desc = it.desc)
    }
}

private fun List<Action>.asActionsFormatted(): List<ActionDto> {
    return this.map {
        ActionDto(
            damageDices = it.damages.mapNotNull { damage ->
                if (damage.damageType == null || damage.damageDice == null) return@mapNotNull null
                DamageDiceDto(
                    dice = damage.damageDice,
                    damage = damage.damageType.index.getDamage()
                )
            },
            attackBonus = it.attackBonus,
            description = it.desc,
            name = it.name
        )
    }
}
