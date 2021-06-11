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

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.ProficiencyEntity
import br.alexandregpereira.hunter.domain.model.Proficiency

internal fun List<ProficiencyEntity>.toDomain(): List<Proficiency> {
    return this.map {
        Proficiency(index = it.index, modifier = it.modifier, name = it.name)
    }
}

internal fun List<Proficiency>.toEntity(): List<ProficiencyEntity> {
    return this.map {
        ProficiencyEntity(index = it.index, modifier = it.modifier, name = it.name)
    }
}
