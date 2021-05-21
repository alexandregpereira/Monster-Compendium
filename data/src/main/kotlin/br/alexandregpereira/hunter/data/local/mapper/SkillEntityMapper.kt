/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.ProficiencyEntity
import br.alexandregpereira.hunter.domain.model.Proficiency

internal fun List<ProficiencyEntity>.toDomain(): List<Proficiency> {
    return this.map {
        Proficiency(index = it.index, modifier = it.modifier, name = it.name)
    }
}

internal fun List<Proficiency>.toEntity(): List<ProficiencyEntity> {
    return this.map {
        ProficiencyEntity(index = it.index, modifier = it.modifier, name = it.name)
    }
}
