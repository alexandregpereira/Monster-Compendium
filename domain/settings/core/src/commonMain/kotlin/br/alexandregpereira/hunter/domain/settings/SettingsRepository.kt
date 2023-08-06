/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
