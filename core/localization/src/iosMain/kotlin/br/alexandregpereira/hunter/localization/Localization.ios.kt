package br.alexandregpereira.hunter.localization

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

internal actual fun getDeviceLangCode(): String {
    return NSLocale.currentLocale.localeIdentifier.replace("_", "-").lowercase()
}
