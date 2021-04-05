package br.alexandregpereira.hunter.data.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {

    fun getLastCompendiumScrollItemPosition(): Flow<Int>
    fun saveCompendiumScrollItemPosition(position: Int): Flow<Unit>
}