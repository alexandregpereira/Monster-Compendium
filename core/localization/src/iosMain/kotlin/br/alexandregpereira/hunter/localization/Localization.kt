package br.alexandregpereira.hunter.localization

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

internal actual fun getDeviceLangCode(): String {
    return NSLocale.currentLocale.languageCode.lowercase()
}
