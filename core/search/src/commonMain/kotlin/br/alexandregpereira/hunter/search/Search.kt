/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.search

fun String.removeAccents(): String {
    val accentMappings = mapOf(
        'á' to 'a', 'é' to 'e', 'í' to 'i', 'ó' to 'o', 'ú' to 'u',
        'â' to 'a', 'ê' to 'e', 'î' to 'i', 'ô' to 'o', 'û' to 'u',
        'ã' to 'a', 'õ' to 'o', 'ç' to 'c',
        'à' to 'a', 'è' to 'e', 'ì' to 'i', 'ò' to 'o', 'ù' to 'u',
        'Á' to 'A', 'É' to 'E', 'Í' to 'I', 'Ó' to 'O', 'Ú' to 'U',
        'Â' to 'A', 'Ê' to 'E', 'Î' to 'I', 'Ô' to 'O', 'Û' to 'U',
        'Ã' to 'A', 'Õ' to 'O', 'Ç' to 'C',
        'À' to 'A', 'È' to 'E', 'Ì' to 'I', 'Ò' to 'O', 'Ù' to 'U'
    )

    return map { char -> accentMappings[char] ?: char }.joinToString("")
}
