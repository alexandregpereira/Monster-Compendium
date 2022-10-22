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

package br.alexandregpereira.hunter.data.monster.remote.mapper

import br.alexandregpereira.hunter.data.monster.remote.model.DamageDto
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageType

internal fun List<DamageDto>.toDomain(): List<Damage> {
    return this.map {
        it.asDomain()
    }
}

internal fun DamageDto.asDomain(): Damage {
    return Damage(index = this.index, type = DamageType.valueOf(this.type.name), name = this.name)
}
