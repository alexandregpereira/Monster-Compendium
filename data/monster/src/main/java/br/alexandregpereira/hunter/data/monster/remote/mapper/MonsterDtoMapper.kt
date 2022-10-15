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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.ColorDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import br.alexandregpereira.hunter.data.monster.spell.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Stats
import java.util.Locale

internal fun List<MonsterDto>.toDomain(monsterImageDtos: List<MonsterImageDto>): List<Monster> {
    return this.map {
        val monsterImage = monsterImageDtos.firstOrNull { monsterImageDto ->
            monsterImageDto.monsterIndex == it.index
        } ?: MonsterImageDto(
            monsterIndex = it.index,
            backgroundColor = ColorDto(light = "#e0dfd1", dark = "#e0dfd1"),
            isHorizontalImage = false,
            imageUrl = DEFAULT_IMAGE_BASE_URL + "default-${it.type.name.lowercase()}.png"
        )

        Monster(
            preview = MonsterPreview(
                index = it.index,
                type = MonsterType.valueOf(it.type.name),
                challengeRating = it.challengeRating,
                name = it.name,
                imageData = MonsterImageData(
                    url = monsterImage.imageUrl,
                    backgroundColor = monsterImage.backgroundColor.toDomain(),
                    isHorizontal = monsterImage.isHorizontalImage
                ),
            ),
            subtype = it.subtype,
            group = it.group,
            subtitle = it.subtitle,
            size = it.size.name.lowercase(Locale.ROOT)
                .replaceFirstChar { char -> char.titlecase(Locale.ROOT) },
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
            reactions = it.reactions.toDomain(),
            sourceName = it.source.name,
            spellcastings = it.spellcastings.toDomain()
        )
    }
}

private const val DEFAULT_IMAGE_BASE_URL =
    "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/"
