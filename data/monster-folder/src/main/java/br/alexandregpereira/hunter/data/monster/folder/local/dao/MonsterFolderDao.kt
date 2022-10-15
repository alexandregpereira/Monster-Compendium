/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.folder.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity

@Dao
interface MonsterFolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMonsterToFolder(folderCrossRefEntity: MonsterFolderEntity)

    @Transaction
    @Query(
        "SELECT  * FROM MonsterFolderEntity " +
                "INNER JOIN MonsterEntity " +
                "on MonsterEntity.`index` == MonsterFolderEntity.`monsterIndex` " +
                "ORDER BY createdAt"
    )
    suspend fun getMonsterFolders(): Map<MonsterFolderEntity, List<MonsterCompleteEntity>>

    @Transaction
    @Query(
        "SELECT  * FROM MonsterFolderEntity " +
                "INNER JOIN MonsterEntity " +
                "on MonsterEntity.`index` == MonsterFolderEntity.`monsterIndex` " +
                "WHERE MonsterFolderEntity.folderName == :folderName " +
                "ORDER BY createdAt"
    )
    suspend fun getMonstersFromFolder(folderName: String): Map<MonsterFolderEntity, List<MonsterCompleteEntity>>

    @Query("DELETE FROM MonsterFolderEntity WHERE folderName == :folderName AND monsterIndex == :monsterIndex")
    suspend fun removeMonsterFromFolder(folderName: String, monsterIndex: String)
}
