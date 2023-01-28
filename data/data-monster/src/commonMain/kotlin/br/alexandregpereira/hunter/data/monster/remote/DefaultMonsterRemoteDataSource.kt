/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import kotlinx.coroutines.flow.Flow

internal class DefaultMonsterRemoteDataSource : MonsterRemoteDataSource {
    override fun getMonsters(lang: String): Flow<List<MonsterDto>> {
        TODO("Not yet implemented")
    }

    override fun getMonsters(sourceAcronym: String, lang: String): Flow<List<MonsterDto>> {
        TODO("Not yet implemented")
    }

    override fun getMonsterImages(jsonUrl: String): Flow<List<MonsterImageDto>> {
        TODO("Not yet implemented")
    }
}
