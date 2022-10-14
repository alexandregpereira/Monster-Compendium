package br.alexandregpereira.hunter.data.settings

import android.content.SharedPreferences
import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun saveSettings(values: Map<String, String>): Flow<Unit> {
        return flow {
            val editor = sharedPreferences.edit()
            values.forEach { entry ->
                editor.putString(entry.key, entry.value).apply()
            }
            emit(Unit)
        }
    }

    override fun getSettingsValue(key: String, defaultValue: String): Flow<String> {
        return flow {
            emit(sharedPreferences.getString(key, defaultValue) ?: defaultValue)
        }
    }
}
