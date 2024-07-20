package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

fun interface IsFirstTime {
    suspend operator fun invoke(): Boolean
}

fun interface ResetFirstTime {
    suspend operator fun invoke()
}

private const val IsFirstTimeKey = "isFirstTimeKey"


internal fun IsFirstTime(
    repository: SettingsRepository,
): IsFirstTime = IsFirstTime {
    repository.getInt(key = IsFirstTimeKey).map { it ?: 1 }.map { it == 1 }.single()
}

internal fun ResetFirstTime(
    repository: SettingsRepository,
): ResetFirstTime = ResetFirstTime {
    repository.saveInt(key = IsFirstTimeKey, value = 0).single()
}
