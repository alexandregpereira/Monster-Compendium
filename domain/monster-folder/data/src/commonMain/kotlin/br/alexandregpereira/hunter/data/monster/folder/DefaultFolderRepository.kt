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

package br.alexandregpereira.hunter.data.monster.folder

import br.alexandregpereira.hunter.data.monster.folder.local.MonsterFolderLocalDataSource
import br.alexandregpereira.hunter.domain.folder.FolderMonsterPreviewRepository
import br.alexandregpereira.hunter.domain.folder.MonsterFolderRepository
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.domain.settings.AppSettingsImageContentScale
import br.alexandregpereira.hunter.domain.settings.GetAppearanceSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal class DefaultFolderRepository(
    private val monsterFolderLocalDataSource: MonsterFolderLocalDataSource,
    private val getAppearanceSettings: GetAppearanceSettings,
) : MonsterFolderRepository, FolderMonsterPreviewRepository {

    override fun addMonsters(folderName: String, indexes: List<String>): Flow<Unit> {
        return monsterFolderLocalDataSource.addMonsters(folderName, monsterIndexes = indexes)
    }

    override fun getMonsterFolders(): Flow<List<MonsterFolder>> {
        return monsterFolderLocalDataSource.getMonsterFolders().map {
            it.asDomain(getImageContentScale())
        }
    }

    override fun getMonstersFromFolder(folderName: String): Flow<MonsterFolder?> {
        return monsterFolderLocalDataSource.getMonstersFromFolder(folderName).map {
            it?.asDomain(getImageContentScale())
        }
    }

    override fun getMonstersFromFolders(foldersName: List<String>): Flow<List<MonsterPreviewFolder>> {
        return monsterFolderLocalDataSource.getMonstersFromFolders(foldersName)
            .map { it.asDomainMonsterPreviewFolderEntity(getImageContentScale()) }
    }

    override fun removeMonsters(folderName: String, indexes: List<String>): Flow<Unit> {
        return monsterFolderLocalDataSource.removeMonsters(folderName, monsterIndexes = indexes)
    }

    override fun getFolderMonsterPreviewsByIds(
        indexes: List<String>
    ): Flow<List<MonsterPreviewFolder>> {
        return monsterFolderLocalDataSource.getFolderMonsterPreviewsByIds(indexes)
            .map { it.asDomainMonsterPreviewFolderEntity(getImageContentScale()) }
    }

    override fun removeMonsterFolders(folderNames: List<String>): Flow<Unit> {
        return monsterFolderLocalDataSource.removeMonsterFolders(folderNames)
    }

    private suspend fun getImageContentScale(): MonsterPreviewFolderImageContentScale {
        return getAppearanceSettings().single().let { settings ->
            when (settings.imageContentScale) {
                AppSettingsImageContentScale.Fit -> MonsterPreviewFolderImageContentScale.Fit
                AppSettingsImageContentScale.Crop -> MonsterPreviewFolderImageContentScale.Crop
            }
        }
    }
}
