/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PreferencesDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : PreferencesDataSource {

    override fun getInt(key: String, defaultValue: Int): Flow<Int> {
        return flow {
            emit(sharedPreferences.getInt(key, defaultValue))
        }
    }

    override fun getString(key: String, defaultValue: String): Flow<String> {
        return flow {
            emit(sharedPreferences.getString(key, defaultValue) ?: defaultValue)
        }
    }

    override fun save(key: String, value: Any): Flow<Unit> {
        return flow {
            val editor = sharedPreferences.edit()
            when (value) {
                is Int -> editor.putInt(key, value).apply()
                is String -> editor.putString(key, value).apply()
                else -> throw UnsupportedOperationException("${value::class} not supported")
            }
            emit(Unit)
        }
    }
}
