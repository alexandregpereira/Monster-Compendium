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

package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.bestiary.getSourceFromBestiary
import br.alexandregpereira.hunter.data.remote.model.AlternativeSourceDto
import br.alexandregpereira.hunter.data.remote.model.SourceDto
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@FlowPreview
suspend fun main() = start {
    getSourceFromBestiary()
        .map { sources ->
            sources.map {
                AlternativeSourceDto(
                    source = SourceDto(it.name, it.acronym),
                    totalMonsters = it.monster.size
                )
            }.sortedBy { it.source.name }
        }
        .collect { sources ->
            saveJsonFile(sources, ALTERNATIVE_SOURCES_JSON_FILE_NAME)
        }
}
