package br.alexandregpereira.hunter.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsSpellDataRepository {

    fun deleteData(): Flow<Unit>
}
