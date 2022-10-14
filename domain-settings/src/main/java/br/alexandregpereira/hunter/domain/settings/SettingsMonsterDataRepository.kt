package br.alexandregpereira.hunter.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsMonsterDataRepository {

    fun deleteData(): Flow<Unit>
}
