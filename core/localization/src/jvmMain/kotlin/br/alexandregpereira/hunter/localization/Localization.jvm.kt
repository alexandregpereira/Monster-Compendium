package br.alexandregpereira.hunter.localization

import java.util.Locale

internal actual fun getDeviceLangCode(): String {
    return Locale.getDefault().toLanguageTag().lowercase()
}
