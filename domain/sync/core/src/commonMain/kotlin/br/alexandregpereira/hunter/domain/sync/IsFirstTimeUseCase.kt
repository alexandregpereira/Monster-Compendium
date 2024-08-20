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

package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

fun interface IsFirstTime {
    suspend operator fun invoke(): Boolean
}

fun interface ResetFirstTime {
    suspend operator fun invoke()
}

private const val IsFirstTimeKey = "isFirstTimeKey"


internal fun IsFirstTime(
    repository: SettingsRepository,
): IsFirstTime = IsFirstTime {
    repository.getInt(key = IsFirstTimeKey).map { it ?: 1 }.map { it == 1 }.single()
}

internal fun ResetFirstTime(
    repository: SettingsRepository,
): ResetFirstTime = ResetFirstTime {
    repository.saveInt(key = IsFirstTimeKey, value = 0).single()
}
