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

package br.alexandregpereira.hunter.data.settings

import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

internal class DefaultSettingsRepository(
    private val settings: Settings
) : SettingsRepository {

    override fun saveString(key: String, value: String): Flow<Unit> = flow {
        emit(settings.putString(key, value))
    }

    override fun saveInt(key: String, value: Int): Flow<Unit> = flow {
        emit(settings.putInt(key, value))
    }

    override fun saveSettings(values: Map<String, String>): Flow<Unit> {
        return flow {
            values.forEach { entry ->
                saveString(entry.key, entry.value).single()
            }
            emit(Unit)
        }
    }

    override fun getInt(key: String): Flow<Int?> = flow {
        emit(settings[key])
    }

    override fun getString(key: String): Flow<String?> = flow {
        emit(settings[key])
    }
}
