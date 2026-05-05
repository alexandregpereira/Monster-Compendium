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
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.domain.model.AbilityDescription

internal fun List<SpecialAbilityCompleteEntity>.toDomain(): List<AbilityDescription> {
    return this.map {
        AbilityDescription(
            name = it.specialAbility.name,
            description = it.specialAbility.description,
            index = "${it.specialAbility.monsterIndex}-${it.specialAbility.name}",
            savingThrows = it.savingThrows.toDomain(),
            conditions = it.conditions.toConditionDomain(),
        )
    }
}

internal fun List<ReactionEntity>.toDomainReactionEntity(): List<AbilityDescription> {
    return this.map {
        AbilityDescription(
            name = it.name,
            description = it.description,
            index = "${it.monsterIndex}-${it.name}",
            savingThrows = emptyList(),
            conditions = emptyList(),
        )
    }
}

internal fun List<AbilityDescription>.toEntity(monsterIndex: String): List<SpecialAbilityCompleteEntity> {
    return this.map {
        val abilityKey = "$monsterIndex-${it.name}"
        SpecialAbilityCompleteEntity(
            specialAbility = SpecialAbilityEntity(
                name = it.name,
                description = it.description,
                monsterIndex = monsterIndex,
            ),
            savingThrows = it.savingThrows.toSavingThrowEntity(abilityKey),
            conditions = it.conditions.toEntity(abilityKey),
        )
    }
}
