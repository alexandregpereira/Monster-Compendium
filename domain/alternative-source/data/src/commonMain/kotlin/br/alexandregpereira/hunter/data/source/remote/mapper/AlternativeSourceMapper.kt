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

package br.alexandregpereira.hunter.data.source.remote.mapper

import br.alexandregpereira.hunter.data.source.remote.model.AlternativeSourceDto
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source

internal fun List<AlternativeSourceDto>.toDomain(): List<AlternativeSource> {
    return this.map {
        AlternativeSource(
            source = Source(
                name = it.source.name,
                acronym = it.source.acronym,
                originalName = it.source.originalName
            ),
            totalMonsters = it.totalMonsters,
            summary = it.summary,
            coverImageUrl = it.coverImageUrl,
            isEnabled = it.isEnabled,
            isLoreEnabled = it.isLoreEnabled
        )
    }
}
