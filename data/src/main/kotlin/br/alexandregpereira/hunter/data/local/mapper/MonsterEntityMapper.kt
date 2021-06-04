/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.local.entity.ProficiencyEntity
import br.alexandregpereira.hunter.data.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.Stats

internal fun List<MonsterEntity>.toDomain(): List<Monster> {
    return this.map {
        Monster(
            preview = MonsterPreview(
                index = it.index,
                type = MonsterType.valueOf(it.type),
                challengeRating = it.challengeRating,
                name = it.name,
                imageData = MonsterImageData(
                    url = it.imageUrl,
                    backgroundColor = Color(
                        light = it.backgroundColorLight,
                        dark = it.backgroundColorDark
                    ),
                    isHorizontal = it.isHorizontalImage
                )
            ),
            subtype = it.subtype,
            group = it.group,
            subtitle = it.subtitle,
            size = it.size,
            alignment = it.alignment,
            stats = Stats(
                armorClass = it.armorClass,
                hitPoints = it.hitPoints,
                hitDice = it.hitDice
            ),
            speed = it.speedEntity.toDomain(),
            abilityScores = it.abilityScores.toObjFromJson<List<AbilityScoreEntity>>().toDomain(),
            savingThrows = it.savingThrows.toObjFromJson<List<ProficiencyEntity>>().toDomain(),
            skills = it.skills.toObjFromJson<List<ProficiencyEntity>>().toDomain(),
            damageVulnerabilities = it.damageVulnerabilities.toObjFromJson<List<ValueEntity>>()
                .toDamageDomain(),
            damageResistances = it.damageResistances.toObjFromJson<List<ValueEntity>>()
                .toDamageDomain(),
            damageImmunities = it.damageImmunities.toObjFromJson<List<ValueEntity>>()
                .toDamageDomain(),
            conditionImmunities = it.conditionImmunities.toObjFromJson<List<ValueEntity>>()
                .toConditionDomain(),
            senses = it.senses.toObjFromJson(),
            languages = it.languages,
            specialAbilities = it.specialAbilities.toObjFromJson<List<SpecialAbilityEntity>>()
                .toDomain(),
            actions = it.actions.toObjFromJson<List<ActionEntity>>().toDomain()
        )
    }
}

internal fun List<Monster>.toEntity(): List<MonsterEntity> {
    return this.map {
        MonsterEntity(
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
            speedEntity = it.speed.toEntity(),
            abilityScores = it.abilityScores.toEntity().toJsonFromObj(),
            savingThrows = it.savingThrows.toEntity().toJsonFromObj(),
            skills = it.skills.toEntity().toJsonFromObj(),
            damageVulnerabilities = it.damageVulnerabilities.toEntity().toJsonFromObj(),
            damageResistances = it.damageResistances.toEntity().toJsonFromObj(),
            damageImmunities = it.damageImmunities.toEntity().toJsonFromObj(),
            conditionImmunities = it.conditionImmunities.toEntity().toJsonFromObj(),
            senses = it.senses.toJsonFromObj(),
            languages = it.languages,
            specialAbilities = it.specialAbilities.toEntity().toJsonFromObj(),
            actions = it.actions.toEntity().toJsonFromObj()
        )
    }
}