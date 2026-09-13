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

package br.alexandregpereira.hunter.home.domain

import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal data class HomeExtraContentProgress(
    val added: Int,
    val total: Int,
)

internal fun interface GetHomeExtraContentProgress {
    operator fun invoke(): Flow<HomeExtraContentProgress>
}

internal fun GetHomeExtraContentProgress(
    getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase,
): GetHomeExtraContentProgress = GetHomeExtraContentProgress {
    getAlternativeSourcesUseCase(onlyContentEnabled = true)
        .map { it.toHomeExtraContentProgress() }
}

/**
 * The default source is the base content, always added, so only the other sources count as extra
 * content.
 */
internal fun List<AlternativeSource>.toHomeExtraContentProgress(): HomeExtraContentProgress {
    val extraSources = filterNot { it.isDefault }
    return HomeExtraContentProgress(
        added = extraSources.count { it.isAdded },
        total = extraSources.size,
    )
}
