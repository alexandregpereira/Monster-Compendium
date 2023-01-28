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

package br.alexandregpereira.hunter.data.di

import android.content.Context
import androidx.room.Room
import br.alexandregpereira.hunter.data.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "hunter-database")
            .build()
    }
    factory { get<Context>().getSharedPreferences("preferences", Context.MODE_PRIVATE) }
    factory { get<AppDatabase>().abilityScoreDao() }
    factory { get<AppDatabase>().actionDao() }
    factory { get<AppDatabase>().conditionDao() }
    factory { get<AppDatabase>().damageDao() }
    factory { get<AppDatabase>().damageDiceDao() }
    factory { get<AppDatabase>().legendaryActionDao() }
    factory { get<AppDatabase>().monsterDao() }
    factory { get<AppDatabase>().monsterFolderDao() }
    factory { get<AppDatabase>().monsterLoreDao() }
    factory { get<AppDatabase>().savingThrowDao() }
    factory { get<AppDatabase>().skillDao() }
    factory { get<AppDatabase>().specialAbilityDao() }
    factory { get<AppDatabase>().speedDao() }
    factory { get<AppDatabase>().speedValueDao() }
    factory { get<AppDatabase>().reactionDao() }
    factory { get<AppDatabase>().spellDao() }
    factory { get<AppDatabase>().spellcastingDao() }
    factory { get<AppDatabase>().spellUsageDao() }
}
