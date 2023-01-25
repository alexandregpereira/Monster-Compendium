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

package br.alexandregpereira.hunter.data.monster.di

import br.alexandregpereira.hunter.data.monster.MonsterAlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterCacheRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterImageRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterLocalRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterRemoteRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.monster.MonsterSettingsRepositoryImpl
import br.alexandregpereira.hunter.data.monster.SettingsMonsterDataRepositoryImpl
import br.alexandregpereira.hunter.data.monster.cache.MonsterCacheDataSource
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSourceImpl
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesDataSource
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesDataSourceImpl
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesRepository
import br.alexandregpereira.hunter.data.monster.remote.MonsterApi
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSourceImpl
import br.alexandregpereira.hunter.domain.repository.CompendiumRepository
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.repository.MonsterAlternativeSourceRepository
import br.alexandregpereira.hunter.domain.repository.MonsterCacheRepository
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
import br.alexandregpereira.hunter.domain.settings.SettingsMonsterDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.koin.dsl.module
import retrofit2.Retrofit

val monsterDataModule = module {
    single<MonsterRemoteDataSource> { MonsterRemoteDataSourceImpl(get()) }
    single<MonsterLocalDataSource> {
        MonsterLocalDataSourceImpl(
            abilityScoreDao = get(),
            actionDao = get(),
            conditionDao = get(),
            damageDao = get(),
            damageDiceDao = get(),
            monsterDao = get(),
            savingThrowDao = get(),
            skillDao = get(),
            specialAbilityDao = get(),
            speedDao = get(),
            speedValueDao = get(),
            reactionDao = get(),
            spellcastingDao = get(),
            spellUsageDao = get(),
            legendaryActionDao = get()
        )
    }
    single { MonsterCacheDataSource() }
    factory<MonsterRepository> { MonsterRepositoryImpl(get(), get()) }
    factory<MonsterLocalRepository> { MonsterLocalRepositoryImpl(get()) }
    factory<MonsterRemoteRepository> { MonsterRemoteRepositoryImpl(get()) }
    factory<MonsterCacheRepository> { MonsterCacheRepositoryImpl(get()) }
    factory<MonsterSettingsRepository> { MonsterSettingsRepositoryImpl(get()) }
    factory<PreferencesDataSource> { PreferencesDataSourceImpl(get()) }
    single { PreferencesRepository(get()) }
    single<CompendiumRepository> { get<PreferencesRepository>() }
    single<MeasurementUnitRepository> { get<PreferencesRepository>() }
    factory<SettingsMonsterDataRepository> { SettingsMonsterDataRepositoryImpl(get(), get()) }
    factory<MonsterImageRepository> { MonsterImageRepositoryImpl(get(), get()) }
    factory<MonsterAlternativeSourceRepository> { MonsterAlternativeSourceRepositoryImpl(get()) }
    factory<MonsterApi> { get<Retrofit>().create(MonsterApi::class.java) }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindMonsterRemoteDataSource(
        monsterRemoteDataSourceImpl: MonsterRemoteDataSourceImpl
    ): MonsterRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindMonsterLocalDataSource(
        monsterLocalDataSourceImpl: MonsterLocalDataSourceImpl
    ): MonsterLocalDataSource

    @Binds
    abstract fun bindMonsterRepository(
        monsterRepositoryImpl: MonsterRepositoryImpl
    ): MonsterRepository

    @Binds
    abstract fun bindMonsterLocalRepository(
        repository: MonsterLocalRepositoryImpl
    ): MonsterLocalRepository

    @Binds
    abstract fun bindMonsterRemoteRepository(
        repository: MonsterRemoteRepositoryImpl
    ): MonsterRemoteRepository

    @Binds
    abstract fun bindMonsterCacheRepository(
        repository: MonsterCacheRepositoryImpl
    ): MonsterCacheRepository

    @Binds
    abstract fun bindMonsterSettingsRepository(
        monsterSettingsRepositoryImpl: MonsterSettingsRepositoryImpl
    ): MonsterSettingsRepository

    @Binds
    abstract fun bindPreferencesDataSource(
        preferencesDataSourceImpl: PreferencesDataSourceImpl
    ): PreferencesDataSource

    @Singleton
    @Binds
    abstract fun bindCompendiumRepository(
        preferencesRepository: PreferencesRepository
    ): CompendiumRepository

    @Singleton
    @Binds
    abstract fun bindMeasurementUnitRepository(
        preferencesRepository: PreferencesRepository
    ): MeasurementUnitRepository

    @Binds
    abstract fun bindSettingsMonsterDataRepository(
        repository: SettingsMonsterDataRepositoryImpl
    ): SettingsMonsterDataRepository

    @Binds
    abstract fun bindMonsterImageRepository(
        repository: MonsterImageRepositoryImpl
    ): MonsterImageRepository

    @Binds
    abstract fun bindMonsterAlternativeSourceRepository(
        repository: MonsterAlternativeSourceRepositoryImpl
    ): MonsterAlternativeSourceRepository
}
