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
                sourceName = it.sourceName,
                isClone = it.isClone,
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
            reactions = it.reactions.toReactionEntity(it.index),
            spellcastings = it.spellcastings.toEntity(it.index),
            legendaryActions = it.legendaryActions.toLegendaryActionEntity(it.index)
        )
    }
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
