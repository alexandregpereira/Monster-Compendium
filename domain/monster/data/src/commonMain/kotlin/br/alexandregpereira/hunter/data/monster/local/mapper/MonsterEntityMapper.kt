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

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.spell.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.spell.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats

fun List<MonsterCompleteEntity>.toDomain(): List<Monster> {
    return this.map {
        it.toDomain()
    }
}

fun List<MonsterEntity>.toDomainMonsterEntity(): List<Monster> {
    return this.map {
        it.toDomain()
    }
}

internal fun MonsterCompleteEntity.toDomain(): Monster {
    val monster = this.monster
    return monster.toDomain().copy(
        speed = this.speed?.toDomain() ?: Speed(hover = false, values = emptyList()),
        abilityScores = this.abilityScores.toDomain(),
        savingThrows = this.savingThrows.toDomain(),
        skills = this.skills.toDomain(),
        damageVulnerabilities = this.damageVulnerabilities.toDamageDomain(),
        damageResistances = this.damageResistances.toDamageDomain(),
        damageImmunities = this.damageImmunities.toDamageDomain(),
        conditionImmunities = this.conditionImmunities.toConditionDomain(),
        specialAbilities = this.specialAbilities.toDomain(),
        actions = this.actions.toDomain(),
        reactions = this.reactions.toDomainReactionEntity(),
        spellcastings = this.spellcastings.toDomain(),
        legendaryActions = this.legendaryActions.toDomain()
    )
}

internal fun Monster.toEntity(): MonsterCompleteEntity {
    return MonsterCompleteEntity(
        monster = MonsterEntity(
            index = index,
            type = type.name,
            subtype = subtype,
            group = group,
            challengeRating = challengeRating,
            name = name,
            subtitle = subtitle,
            imageUrl = imageData.url,
            backgroundColorLight = imageData.backgroundColor.light,
            backgroundColorDark = imageData.backgroundColor.dark,
            isHorizontalImage = imageData.isHorizontal,
            size = size,
            alignment = alignment,
            armorClass = stats.armorClass,
            hitPoints = stats.hitPoints,
            hitDice = stats.hitDice,
            senses = senses.joinToString(),
            languages = languages,
            sourceName = sourceName,
            isClone = isClone,
        ),
        speed = speed.toEntity(index),
        abilityScores = toAbilityScoreEntity(),
        savingThrows = savingThrows.toSavingThrowEntity(index),
        skills = skills.toSkillEntity(index),
        damageVulnerabilities = damageVulnerabilities.toDamageVulnerabilityEntity(index),
        damageResistances = damageResistances.toDamageResistanceEntity(index),
        damageImmunities = damageImmunities.toDamageImmunityEntity(index),
        conditionImmunities = conditionImmunities.toEntity(index),
        specialAbilities = specialAbilities.toEntity(index),
        actions = actions.toEntity(index),
        reactions = reactions.toReactionEntity(index),
        spellcastings = spellcastings.toEntity(index),
        legendaryActions = legendaryActions.toLegendaryActionEntity(index)
    )
}

internal fun List<Monster>.toEntity(): List<MonsterCompleteEntity> {
    return this.map { it.toEntity() }
}

private fun MonsterEntity.toDomain(): Monster {
    val monster = this
    return Monster(
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
        sourceName = monster.sourceName,
        senses = monster.senses.split(", "),
        languages = monster.languages,
        isClone = monster.isClone
    )
}
