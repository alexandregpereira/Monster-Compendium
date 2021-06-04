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

    override fun getLastCompendiumScrollItemPosition(): Flow<Int> {
        return flow {
            emit(sharedPreferences.getInt(COMPENDIUM_SCROLL_ITEM_POSITION_KEY, 0))
        }
    }

    override fun saveCompendiumScrollItemPosition(position: Int): Flow<Unit> {
        return flow {
            sharedPreferences.edit().putInt(COMPENDIUM_SCROLL_ITEM_POSITION_KEY, position).apply()
            emit(Unit)
        }
    }
}

private const val COMPENDIUM_SCROLL_ITEM_POSITION_KEY = "COMPENDIUM_SCROLL_ITEM_POSITION_KEY"
