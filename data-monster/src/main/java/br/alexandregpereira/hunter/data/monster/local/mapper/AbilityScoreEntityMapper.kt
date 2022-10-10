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

import br.alexandregpereira.hunter.data.monster.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Monster

internal fun List<AbilityScoreEntity>.toDomain(): List<AbilityScore> {
    return this.map {
        AbilityScore(
            type = it.type.toDomain(),
            value = it.value,
            modifier = it.modifier
        )
    }
}

internal fun String.toDomain(): AbilityScoreType {
    return AbilityScoreType.valueOf(this)
}

internal fun Monster.toAbilityScoreEntity(): List<AbilityScoreEntity> {
    val abilityScores = this.abilityScores
    return abilityScores.map {
        AbilityScoreEntity(
            type = it.type.toEntity(),
            value = it.value,
            modifier = it.modifier,
            monsterIndex = this.index
        )
    }
}

internal fun AbilityScoreType.toEntity(): String {
    return this.name
}