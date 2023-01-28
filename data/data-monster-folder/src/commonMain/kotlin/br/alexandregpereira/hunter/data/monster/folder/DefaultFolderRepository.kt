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

package br.alexandregpereira.hunter.data.monster.folder

import br.alexandregpereira.hunter.domain.folder.FolderMonsterPreviewRepository
import br.alexandregpereira.hunter.domain.folder.MonsterFolderRepository
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import kotlinx.coroutines.flow.Flow

internal class DefaultFolderRepository : MonsterFolderRepository, FolderMonsterPreviewRepository {

    override fun getFolderMonsterPreviewsByIds(indexes: List<String>): Flow<List<MonsterPreviewFolder>> {
        TODO("Not yet implemented")
    }

    override fun addMonsters(folderName: String, indexes: List<String>): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun removeMonsters(folderName: String, indexes: List<String>): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun getMonsterFolders(): Flow<List<MonsterFolder>> {
        TODO("Not yet implemented")
    }

    override fun getMonstersFromFolder(folderName: String): Flow<MonsterFolder?> {
        TODO("Not yet implemented")
    }

    override fun removeMonsterFolders(folderNames: List<String>): Flow<Unit> {
        TODO("Not yet implemented")
    }
}
