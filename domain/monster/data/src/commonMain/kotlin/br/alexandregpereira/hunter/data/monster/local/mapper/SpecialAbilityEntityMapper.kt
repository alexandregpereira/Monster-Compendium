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

import br.alexandregpereira.hunter.data.monster.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription

internal fun List<SpecialAbilityEntity>.toDomain(): List<AbilityDescription> {
    return this.map {
        AbilityDescription(
            name = it.name,
            description = it.description,
            index = "${it.monsterIndex}-${it.name}",
        )
    }
}

internal fun List<ReactionEntity>.toDomainReactionEntity(): List<AbilityDescription> {
    return this.map {
        AbilityDescription(
            name = it.name,
            description = it.description,
            index = "${it.monsterIndex}-${it.name}",
        )
    }
}

internal fun List<AbilityDescription>.toEntity(monsterIndex: String): List<SpecialAbilityEntity> {
    return this.map {
        SpecialAbilityEntity(
            name = it.name,
            description = it.description,
            monsterIndex = monsterIndex,
        )
    }
}
