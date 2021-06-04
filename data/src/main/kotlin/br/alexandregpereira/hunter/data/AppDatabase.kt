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

package br.alexandregpereira.hunter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.alexandregpereira.hunter.data.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity

@Database(entities = [MonsterEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun monsterDao(): MonsterDao
}