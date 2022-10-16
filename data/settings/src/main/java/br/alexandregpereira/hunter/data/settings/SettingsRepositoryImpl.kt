/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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

import android.content.SharedPreferences
import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun saveSettings(values: Map<String, String>): Flow<Unit> {
        return flow {
            val editor = sharedPreferences.edit()
            values.forEach { entry ->
                editor.putString(entry.key, entry.value).apply()
            }
            emit(Unit)
        }
    }

    override fun getSettingsValue(key: String, defaultValue: String): Flow<String> {
        return flow {
            emit(sharedPreferences.getString(key, defaultValue) ?: defaultValue)
        }
    }
}
