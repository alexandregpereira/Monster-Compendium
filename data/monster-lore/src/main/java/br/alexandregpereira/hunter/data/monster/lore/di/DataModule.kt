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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.monster.lore.di

import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.MonsterLoreSourceRepositoryImpl
import br.alexandregpereira.hunter.data.monster.lore.local.MonsterLoreLocalDataSource
import br.alexandregpereira.hunter.data.monster.lore.remote.MonsterLoreApi
import br.alexandregpereira.hunter.data.monster.lore.remote.MonsterLoreRemoteDataSource
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSettingsRepository
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreSourceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.koin.dsl.module
import retrofit2.Retrofit

val monsterLoreDataModule = module {
    factory { MonsterLoreLocalDataSource(get()) }
    factory { MonsterLoreRemoteDataSource(get()) }
    single<MonsterLoreRepository> { MonsterLoreRepositoryImpl(get(), get()) }
    factory<MonsterLoreSettingsRepository> { MonsterLoreSettingsRepositoryImpl(get()) }
    factory<MonsterLoreSourceRepository> { MonsterLoreSourceRepositoryImpl(get()) }
    factory { get<Retrofit>().create(MonsterLoreApi::class.java) }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindMonsterLoreRepository(
        repository: MonsterLoreRepositoryImpl
    ): MonsterLoreRepository

    @Binds
    abstract fun bindMonsterLoreSettingsRepository(
        repository: MonsterLoreSettingsRepositoryImpl
    ): MonsterLoreSettingsRepository

    @Binds
    abstract fun bindMonsterLoreSourceRepository(
        repository: MonsterLoreSourceRepositoryImpl
    ): MonsterLoreSourceRepository
}
