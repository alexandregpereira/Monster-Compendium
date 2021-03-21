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

package br.alexandregpereira.hunter.data.mapper

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType

internal fun List<MonsterDto>.toDomain(): List<Monster> {
    return this.map {
        Monster(
            index = it.index,
            type = MonsterType.valueOf(it.type.name),
            challengeRating = it.challengeRating,
            name = it.name,
            subtitle = it.subtitle,
            imageData = MonsterImageData(url = it.imageUrl, backgroundColor = it.backgroundColor),
            size = it.size,
            alignment = it.alignment,
            subtype = it.subtype,
            armorClass = it.armorClass,
            hitPoints = it.hitPoints,
            hitDice = it.hitDice,
            speed = it.speed.toDomain(),
            abilityScores = it.abilityScores.toDomain(),
            savingThrows = it.savingThrows.toDomain(),
            skills = it.skills.toDomain(),
            damageVulnerabilities = it.damageVulnerabilities.toDomain(),
            damageResistances = it.damageResistances.toDomain(),
            damageImmunities = it.damageImmunities.toDomain()
        )
    }
}
