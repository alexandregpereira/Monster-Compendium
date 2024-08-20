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

package br.alexandregpereira.hunter.data.source.local.mapper

import br.alexandregpereira.hunter.data.source.local.entity.AlternativeSourceEntity
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import kotlinx.datetime.Clock

internal fun List<AlternativeSourceEntity>.toDomain(): List<AlternativeSource> {
    return this.map {
        AlternativeSource(
            source = Source(
                acronym = it.acronym,
                name = "",
                originalName = ""
            ),
            totalMonsters = 0,
            summary = "",
            coverImageUrl = "",
            isEnabled = true,
            isLoreEnabled = true,
        )
    }
}

internal fun String.toEntity(): AlternativeSourceEntity {
    return AlternativeSourceEntity(
        acronym = this,
        createdAt = Clock.System.now().toEpochMilliseconds()
    )
}
