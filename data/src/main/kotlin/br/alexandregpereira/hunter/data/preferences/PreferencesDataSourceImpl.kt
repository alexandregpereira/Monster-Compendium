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
