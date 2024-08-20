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

package br.alexandregpereira.hunter.domain.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsRepository {

    fun saveString(key: String, value: String): Flow<Unit>

    fun saveInt(key: String, value: Int): Flow<Unit>

    fun saveSettings(values: Map<String, String>): Flow<Unit>

    fun getString(key: String): Flow<String?>

    fun getInt(key: String): Flow<Int?>
}

inline fun <reified T : Any> SettingsRepository.getValue(
    key: String,
    defaultValue: T? = null
): Flow<T> = when (T::class) {
    Int::class -> getInt(key).orDefault(defaultValue ?: 0).map { it as T }
    String::class -> getString(key).orDefault(defaultValue ?: "").map { it as T }
    else -> throw IllegalArgumentException("Invalid type!")
}

inline fun <reified T : Any> SettingsRepository.saveValue(key: String, value: T): Flow<Unit> {
    return when (T::class) {
        Int::class -> saveInt(key, value as Int)
        String::class -> saveString(key, value as String)
        else -> throw IllegalArgumentException("Invalid type!")
    }
}
fun <T> Flow<T?>.orDefault(defaultValue: T): Flow<T> {
    return map { it ?: defaultValue }
}
