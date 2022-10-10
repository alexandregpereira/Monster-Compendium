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

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Source
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats

fun List<MonsterCompleteEntity>.toDomain(): List<Monster> {
    return this.map {
        it.toDomain()
    }
}

internal fun MonsterCompleteEntity.toDomain(): Monster {
    val monster = this.monster
    return Monster(
        preview = MonsterPreview(
            index = monster.index,
            type = MonsterType.valueOf(monster.type),
            challengeRating = monster.challengeRating,
            name = monster.name,
            imageData = MonsterImageData(
                url = monster.imageUrl,
                backgroundColor = Color(
                    light = monster.backgroundColorLight,
                    dark = monster.backgroundColorDark
                ),
                isHorizontal = monster.isHorizontalImage
            )
        ),
        subtype = monster.subtype,
        group = monster.group,
        subtitle = monster.subtitle,
        size = monster.size,
        alignment = monster.alignment,
        stats = Stats(
            armorClass = monster.armorClass,
            hitPoints = monster.hitPoints,
            hitDice = monster.hitDice
        ),
        speed = this.speed?.toDomain() ?: Speed(hover = false, values = emptyList()),
        abilityScores = this.abilityScores.toDomain(),
        savingThrows = this.savingThrows.toDomain(),
        skills = this.skills.toDomain(),
        damageVulnerabilities = this.damageVulnerabilities.toDamageDomain(),
        damageResistances = this.damageResistances.toDamageDomain(),
        damageImmunities = this.damageImmunities.toDamageDomain(),
        conditionImmunities = this.conditionImmunities.toConditionDomain(),
        senses = monster.senses.split(", "),
        languages = monster.languages,
        specialAbilities = this.specialAbilities.toDomain(),
        actions = this.actions.toDomain(),
        reactions = this.reactions.toDomain(),
        source = Source(monster.sourceName, monster.sourceAcronym)
    )
}

internal fun List<Monster>.toEntity(): List<MonsterCompleteEntity> {
    return this.map {
        MonsterCompleteEntity(
            monster = MonsterEntity(
                index = it.index,
                type = it.type.name,
                subtype = it.subtype,
                group = it.group,
                challengeRating = it.challengeRating,
                name = it.name,
                subtitle = it.subtitle,
                imageUrl = it.imageData.url,
                backgroundColorLight = it.imageData.backgroundColor.light,
                backgroundColorDark = it.imageData.backgroundColor.dark,
                isHorizontalImage = it.imageData.isHorizontal,
                size = it.size,
                alignment = it.alignment,
                armorClass = it.stats.armorClass,
                hitPoints = it.stats.hitPoints,
                hitDice = it.stats.hitDice,
                senses = it.senses.joinToString(),
                languages = it.languages,
                sourceName = it.source.name,
                sourceAcronym = it.source.acronym
            ),
            speed = it.speed.toEntity(it.index),
            abilityScores = it.toAbilityScoreEntity(),
            savingThrows = it.savingThrows.toSavingThrowEntity(it.index),
            skills = it.skills.toSkillEntity(it.index),
            damageVulnerabilities = it.damageVulnerabilities.toDamageVulnerabilityEntity(it.index),
            damageResistances = it.damageResistances.toDamageResistanceEntity(it.index),
            damageImmunities = it.damageImmunities.toDamageImmunityEntity(it.index),
            conditionImmunities = it.conditionImmunities.toEntity(it.index),
            specialAbilities = it.specialAbilities.toEntity(it.index),
            actions = it.actions.toEntity(it.index),
            reactions = it.reactions.toReactionEntity(it.index)
        )
    }
}
