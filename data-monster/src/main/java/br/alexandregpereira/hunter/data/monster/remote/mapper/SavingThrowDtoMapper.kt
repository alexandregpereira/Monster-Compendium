/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.SavingThrowDto
import br.alexandregpereira.hunter.domain.model.Proficiency
import java.util.Locale

internal fun List<SavingThrowDto>.toDomain(): List<Proficiency> {
    return this.map {
        val name = it.type.name.lowercase(Locale.ROOT)
            .substring(0..2)
            .replaceFirstChar { char -> char.titlecase(Locale.ROOT) }

        Proficiency(
            index = it.index,
            name = name,
            modifier = it.modifier
        )
    }
}