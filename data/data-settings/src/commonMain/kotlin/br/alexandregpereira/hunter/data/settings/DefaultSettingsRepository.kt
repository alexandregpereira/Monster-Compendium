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
