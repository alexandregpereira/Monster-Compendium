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

package br.alexandregpereira.hunter.data.monster.local.mapper

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus
import br.alexandregpereira.hunter.data.monster.spell.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.spell.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats

fun List<MonsterCompleteEntity>.toDomain(
    monsterImageContentScale: MonsterImageContentScale,
): List<Monster> {
    return this.map {
        it.toDomain(monsterImageContentScale)
    }
}

fun List<MonsterEntity>.toDomainMonsterEntity(
    monsterImageContentScale: MonsterImageContentScale,
): List<Monster> {
    return this.map {
        it.toDomain(monsterImageContentScale)
    }
}

internal fun MonsterCompleteEntity.toDomain(
    monsterImageContentScale: MonsterImageContentScale,
): Monster {
    val monster = this.monster
    return monster.toDomain(monsterImageContentScale).copy(
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
            status = status.toEntityStatus(),
            imageContentScale = when (imageData.contentScale) {
                MonsterImageContentScale.Fit -> 0
                MonsterImageContentScale.Crop -> 1
            },
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
        legendaryActions = legendaryActions.toEntity(index)
    )
}

internal fun MonsterStatus.toEntityStatus(): MonsterEntityStatus {
    return when (this) {
        MonsterStatus.Original -> MonsterEntityStatus.Original
        MonsterStatus.Clone -> MonsterEntityStatus.Clone
        MonsterStatus.Edited -> MonsterEntityStatus.Edited
        MonsterStatus.Imported -> MonsterEntityStatus.Imported
    }
}

internal fun List<Monster>.toEntity(): List<MonsterCompleteEntity> {
    return this.map { it.toEntity() }
}

private fun MonsterEntity.toDomain(
    monsterImageContentScale: MonsterImageContentScale,
): Monster {
    val monster = this
    val imageContentScale = when (monster.imageContentScale) {
        0 -> MonsterImageContentScale.Fit
        1 -> MonsterImageContentScale.Crop
        else -> null
    }
    return Monster(
        index = monster.index,
        type = MonsterType.valueOf(monster.type),
        challengeRatingData = ChallengeRating(monster.challengeRating),
        name = monster.name,
        imageData = MonsterImageData(
            url = monster.imageUrl.replaceOldImageUrl(),
            backgroundColor = Color(
                light = monster.backgroundColorLight,
                dark = monster.backgroundColorDark
            ),
            isHorizontal = monster.isHorizontalImage,
            contentScale = imageContentScale ?: monsterImageContentScale
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
        status = when (monster.status) {
            MonsterEntityStatus.Original -> MonsterStatus.Original
            MonsterEntityStatus.Clone -> MonsterStatus.Clone
            MonsterEntityStatus.Edited -> MonsterStatus.Edited
            MonsterEntityStatus.Imported -> MonsterStatus.Imported
        }
    )
}

fun String.replaceOldImageUrl(): String {
    val oldImageUrl = "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/"
    val newImageUrl = "$NEW_IMAGE_URL/main/images/"
    return this.replace(oldImageUrl, newImageUrl)
}

private const val NEW_IMAGE_URL = "https://media.githubusercontent.com/media/alexandregpereira/Monster-Compendium-Content"
