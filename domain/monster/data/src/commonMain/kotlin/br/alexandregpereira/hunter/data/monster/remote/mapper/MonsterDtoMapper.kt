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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.spell.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Stats

internal fun List<MonsterDto>.toDomain(): List<Monster> {
    return this.map {
        Monster(
            index = it.index,
            type = MonsterType.valueOf(it.type.name),
            challengeRatingData = ChallengeRating(it.challengeRating),
            name = it.name,
            imageData = MonsterImageData(
                url = "",
                backgroundColor = Color(light = "", dark = ""),
                isHorizontal = false
            ),
            subtype = it.subtype,
            group = it.group,
            subtitle = it.subtitle,
            size = it.size.name.lowercase()
                .replaceFirstChar { char -> char.titlecase() },
            alignment = it.alignment,
            stats = Stats(
                armorClass = it.armorClass,
                hitPoints = it.hitPoints,
                hitDice = it.hitDice
            ),
            speed = it.speed.toDomain(),
            abilityScores = it.abilityScores.toDomain(),
            savingThrows = it.savingThrows.toDomain(),
            skills = it.skills.toDomain(),
            damageVulnerabilities = it.damageVulnerabilities.toDomain(),
            damageResistances = it.damageResistances.toDomain(),
            damageImmunities = it.damageImmunities.toDomain(),
            conditionImmunities = it.conditionImmunities.toDomain(),
            senses = it.senses,
            languages = it.languages,
            specialAbilities = it.specialAbilities.toDomain(),
            actions = it.actions.toDomain(),
            legendaryActions = it.legendaryActions.toDomain(),
            reactions = it.reactions.toDomain(),
            sourceName = it.source.name,
            spellcastings = it.spellcastings.toDomain()
        )
    }
}
