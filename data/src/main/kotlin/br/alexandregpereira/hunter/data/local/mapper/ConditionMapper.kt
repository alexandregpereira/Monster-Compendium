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

import br.alexandregpereira.hunter.data.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType

internal fun List<ValueEntity>.toConditionDomain(): List<Condition> {
    return this.map {
        Condition(index = it.index, type = ConditionType.valueOf(it.type), name = it.name)
    }
}

internal fun List<Condition>.toEntity(): List<ValueEntity> {
    return this.map {
        ValueEntity(index = it.index, type = it.type.name, name = it.name)
    }
}