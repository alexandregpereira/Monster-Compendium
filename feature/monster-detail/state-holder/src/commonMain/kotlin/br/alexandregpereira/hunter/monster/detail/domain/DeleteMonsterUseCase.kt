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

package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import kotlinx.coroutines.flow.Flow

fun interface DeleteMonsterUseCase {
    operator fun invoke(id: String): Flow<Unit>
}

internal fun DeleteMonsterUseCase(
    repository: MonsterLocalRepository,
): DeleteMonsterUseCase = DeleteMonsterUseCase { id ->
    repository.deleteMonster(id)
}
