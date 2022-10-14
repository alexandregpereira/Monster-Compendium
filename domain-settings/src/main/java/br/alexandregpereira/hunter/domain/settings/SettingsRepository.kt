package br.alexandregpereira.hunter.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun saveSettings(values: Map<String, String>): Flow<Unit>
    fun getSettingsValue(key: String, defaultValue: String = ""): Flow<String>
}
