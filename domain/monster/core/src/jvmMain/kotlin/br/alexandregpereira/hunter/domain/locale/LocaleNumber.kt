package br.alexandregpereira.hunter.domain.locale

import java.text.NumberFormat

internal actual fun Int.formatToNumber(): String {
    return NumberFormat.getIntegerInstance().format(this)
}
