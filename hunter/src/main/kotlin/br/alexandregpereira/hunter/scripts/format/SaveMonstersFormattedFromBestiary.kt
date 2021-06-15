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

import br.alexandregpereira.hunter.bestiary.Monster
import br.alexandregpereira.hunter.bestiary.getMonstersFromBestiary
import br.alexandregpereira.hunter.data.remote.model.AbilityScoreDto
import br.alexandregpereira.hunter.data.remote.model.AbilityScoreTypeDto
import br.alexandregpereira.hunter.data.remote.model.ConditionDto
import br.alexandregpereira.hunter.data.remote.model.ConditionTypeDto
import br.alexandregpereira.hunter.data.remote.model.DamageDto
import br.alexandregpereira.hunter.data.remote.model.DamageTypeDto
import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.remote.model.MonsterSizeDto
import br.alexandregpereira.hunter.data.remote.model.MonsterTypeDto
import br.alexandregpereira.hunter.data.remote.model.SavingThrowDto
import br.alexandregpereira.hunter.data.remote.model.SkillDto
import br.alexandregpereira.hunter.data.remote.model.SourceDto
import br.alexandregpereira.hunter.data.remote.model.SpeedDto
import br.alexandregpereira.hunter.data.remote.model.SpeedTypeDto
import br.alexandregpereira.hunter.scripts.MONSTER_JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.saveJsonFile
import br.alexandregpereira.hunter.scripts.start
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import java.io.File
import java.util.Locale

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    getMonstersFromBestiary()
        .map { monsters ->
            monsters.filter { it.cr != null && it.hp.average != null }
                .asMonstersFormatted()
        }
        .single()
        .asSequence()
        .asFlow()
//        .flatMapMerge {
//            it.downloadImage()
//        }
        .toList()
        .filterNotNull()
        .sortedBy { it.name }
        .groupBy { it.source.acronym }
        .let {
            it.forEach { entry ->
                val monsters = entry.value
                println("\n${monsters.size} monsters formatted")
                monsters.forEach { monster ->
                    println("id: ${monster.index}, name: ${monster.name}")
                }

                val source = "json/${entry.key.toLowerCase(Locale.ROOT)}"
                val fileName = "$source/$MONSTER_JSON_FILE_NAME"

                File(source).mkdir()
                saveJsonFile(monsters, fileName, printJson = false)
                return@start
            }
        }
}

private fun List<Monster>.asMonstersFormatted(): List<MonsterDto> {
    return this.mapNotNull {
        runCatching {
            MonsterDto(
                index = it.getIndex(),
                source = it.sourceFormatted(),
                type = it.typeFormatted(),
                subtype = it.subtypeFormatted(),
                group = it.getGroup(),
                challengeRating = it.cr!!.challengeRatingFormatted(),
                name = it.name,
                imageUrl = getImageUrl(it.getIndex()),
                size = MonsterSizeDto.valueOf(it.size.name),
                alignment = it.alignmentFormatted(),
                armorClass = it.ac,
                hitPoints = it.hp.average!!,
                hitDice = it.hp.formula!!.replace(" ", ""),
                speed = it.speedFormatted(),
                abilityScores = it.abilityScoresFormatted(),
                savingThrows = it.savingThrowFormatted(),
                skills = it.skillsFormatted(),
                damageVulnerabilities = it.vulnerable.damagesFormatted(),
                damageResistances = it.resist.damagesFormatted(),
                damageImmunities = it.immune.damagesFormatted(),
                conditionImmunities = it.conditionsImmuneFormatted(),
                senses = it.senses,
                languages = it.languages.joinToString(),
                specialAbilities = listOf(),
                actions = listOf()
            ).formatSubtitle()
        }.getOrElse { error ->
            print("Monster error: ${it.name}")
            error.printStackTrace()
            println()
            null
        }
    }
}

private fun Monster.getIndex(): String {
    return name.replace(" ", "-").toLowerCase(Locale.ROOT)
}

private fun Monster.typeFormatted(): MonsterTypeDto {
    return runCatching {
        MonsterTypeDto.valueOf(type.type.toLowerCase(Locale.ROOT))
    }.getOrDefault(MonsterTypeDto.HUMANOID)
}

private fun Monster.subtypeFormatted(): String? {
    return type.tags.joinToString(" ").takeIf { it.isNotBlank() }
}

private fun Monster.getGroup(): String? {
    val index = getIndex()
    return when {
        isGroupByIndex(index) -> {
            getGroupByIndex(index)
        }
        else -> {
            getGroupByGroupMap(index)
        }
    }
}

private fun String.challengeRatingFormatted(): Float {
    return if (this.contains("/").not()) this.toFloat() else {
        val numbers = this.split("/")
        numbers[0].toFloat() / numbers[1].toFloat()
    }
}

private fun Monster.alignmentFormatted(): String {
    return alignment.joinToString("-")
        .replace("NX-C-G-NY-E", "L-NX-C-G-NY-E")
        .replace("L-NX-C-NY-E", "L-NX-C-G-NY-E")
        .replace("A", "U")
        .run {
            when (this) {
                "A" -> "any alignment"
                "C-E" -> "chaotic evil"
                "C-G" -> "chaotic good"
                "C-G-NY-E" -> "chaotic good or evil"
                "C-N" -> "chaotic neutral"
                "L" -> "any lawful alignment"
                "L-E" -> "lawful evil"
                "L-G" -> "lawful good"
                "L-N" -> "lawful neutral"
                "L-NX-C-E" -> "lawful or chaotic evil"
                "L-NX-C-G-NY-E" -> "lawful or chaotic good or evil"
                "N" -> "neutral"
                "N-E" -> "neutral evil"
                "N-G" -> "neutral good"
                else -> "unaligned"
            }
        }
}

private fun Monster.sourceFormatted(): SourceDto {
    return SourceDto(
        name = sourceName,
        acronym = source
    )
}

private fun Monster.speedFormatted(): SpeedDto {
    val burrow = createSpeedValue(SpeedTypeDto.BURROW, speed.burrow.toString())
    val climb = createSpeedValue(SpeedTypeDto.CLIMB, speed.climb.toString())
    val fly = createSpeedValue(SpeedTypeDto.FLY, speed.fly.toString())
    val walk = createSpeedValue(SpeedTypeDto.WALK, speed.walk.toString())
    val swim = createSpeedValue(SpeedTypeDto.SWIM, speed.swim.toString())

    return SpeedDto(
        hover = speed.canHover,
        values = listOfNotNull(burrow, climb, fly, walk, swim)
    )
}

private fun Monster.abilityScoresFormatted(): List<AbilityScoreDto> {
    val strength = AbilityScoreDto(
        type = AbilityScoreTypeDto.STRENGTH,
        value = str,
        modifier = calculateAbilityScoreModifier(str)
    )
    val dexterity = AbilityScoreDto(
        type = AbilityScoreTypeDto.DEXTERITY,
        value = dex,
        modifier = calculateAbilityScoreModifier(dex)
    )
    val constitution = AbilityScoreDto(
        type = AbilityScoreTypeDto.CONSTITUTION,
        value = con,
        modifier = calculateAbilityScoreModifier(con)
    )
    val intelligence = AbilityScoreDto(
        type = AbilityScoreTypeDto.INTELLIGENCE,
        value = int,
        modifier = calculateAbilityScoreModifier(int)
    )
    val wisdom = AbilityScoreDto(
        type = AbilityScoreTypeDto.WISDOM,
        value = wis,
        modifier = calculateAbilityScoreModifier(wis)
    )
    val charisma = AbilityScoreDto(
        type = AbilityScoreTypeDto.CHARISMA,
        value = cha,
        modifier = calculateAbilityScoreModifier(cha)
    )
    return listOf(strength, dexterity, constitution, intelligence, wisdom, charisma)
}

private fun Monster.savingThrowFormatted(): List<SavingThrowDto> = save?.run {
    val strength = str?.createSavingThrow("str")
    val dexterity = dex?.createSavingThrow("dex")
    val constitution = con?.createSavingThrow("con")
    val intelligence = int?.createSavingThrow("int")
    val wisdom = wis?.createSavingThrow("wis")
    val charisma = cha?.createSavingThrow("cha")

    listOfNotNull(strength, dexterity, constitution, intelligence, wisdom, charisma)
} ?: emptyList()

private fun String.createSavingThrow(type: String): SavingThrowDto {
    val typeEnum = when (type) {
        "str" -> AbilityScoreTypeDto.STRENGTH
        "dex" -> AbilityScoreTypeDto.DEXTERITY
        "con" -> AbilityScoreTypeDto.CONSTITUTION
        "int" -> AbilityScoreTypeDto.INTELLIGENCE
        "wis" -> AbilityScoreTypeDto.WISDOM
        "cha" -> AbilityScoreTypeDto.CHARISMA
        else -> throw IllegalArgumentException("Invalid Saving throw:")
    }

    return SavingThrowDto(
        index = "saving-throw-$type",
        type = typeEnum,
        modifier = this.replace("+", "").toInt()
    )
}

private fun Monster.skillsFormatted(): List<SkillDto> {
    return skill.map {
        SkillDto(
            index = "skill-${it.key.replace(" ", "-")}",
            modifier = it.value.replace("+", "").toInt(),
            name = it.key.capitalize(Locale.ROOT)
        )
    }
}

private fun List<String>.damagesFormatted(): List<DamageDto> {
    return this.map {
        val damageType = runCatching {
            DamageTypeDto.valueOf(it.replace("*", "").toUpperCase(Locale.ROOT))
        }.getOrDefault(DamageTypeDto.OTHER)
        DamageDto(
            index = it.replace("*", ""),
            type = damageType,
            name = it.capitalize(Locale.ROOT)
        )
    }
}

private fun Monster.conditionsImmuneFormatted(): List<ConditionDto> {
    return conditionImmune.map {
        val type = runCatching { ConditionTypeDto.valueOf(it.toUpperCase(Locale.ROOT)) }
            .getOrDefault(ConditionTypeDto.UNCONSCIOUS)
        ConditionDto(
            index = it,
            type = type,
            name = it.capitalize(Locale.ROOT)
        )
    }
}
