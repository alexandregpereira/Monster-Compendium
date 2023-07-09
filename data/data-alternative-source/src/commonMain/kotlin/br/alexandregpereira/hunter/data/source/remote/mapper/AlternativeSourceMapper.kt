/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.source.remote.mapper

import br.alexandregpereira.hunter.data.source.remote.model.AlternativeSourceDto
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source

internal fun List<AlternativeSourceDto>.toDomain(): List<AlternativeSource> {
    return this.map {
        AlternativeSource(
            source = Source(
                name = it.source.name,
                acronym = it.source.acronym,
                originalName = it.source.originalName
            ),
            totalMonsters = it.totalMonsters,
            summary = it.summary,
            coverImageUrl = it.coverImageUrl,
            isEnabled = it.isEnabled
        )
    }
}
