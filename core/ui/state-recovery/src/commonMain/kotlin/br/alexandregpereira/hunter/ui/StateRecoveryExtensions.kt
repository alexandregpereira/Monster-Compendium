/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.ui

fun StateRecovery.getString(key: String): String? {
    return this[key] as? String
}

fun StateRecovery.getInt(key: String): Int? {
    return this[key] as? Int
}

fun StateRecovery.getLong(key: String): Long? {
    return this[key] as? Long
}

fun StateRecovery.getFloat(key: String): Float? {
    return this[key] as? Float
}

fun StateRecovery.getDouble(key: String): Double? {
    return this[key] as? Double
}

fun StateRecovery.getBoolean(key: String): Boolean? {
    return this[key] as? Boolean
}
