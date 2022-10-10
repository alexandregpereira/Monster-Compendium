/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

@file:Suppress("EXPERIMENTAL_API_USAGE")

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.local.MonsterFolderLocalDataSource
import br.alexandregpereira.hunter.data.local.MonsterFolderLocalDataSourceImpl
import br.alexandregpereira.hunter.data.preferences.PreferencesDataSource
import br.alexandregpereira.hunter.data.preferences.PreferencesDataSourceImpl
import br.alexandregpereira.hunter.data.preferences.PreferencesRepository
import br.alexandregpereira.hunter.data.source.AlternativeSourceRemoteDataSource
import br.alexandregpereira.hunter.data.source.AlternativeSourceRemoteDataSourceImpl
import br.alexandregpereira.hunter.data.source.AlternativeSourceRepositoryImpl
import br.alexandregpereira.hunter.domain.repository.AlternativeSourceRepository
import br.alexandregpereira.hunter.domain.repository.CompendiumRepository
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindPreferencesDataSource(
        preferencesDataSourceImpl: PreferencesDataSourceImpl
    ): PreferencesDataSource

    @Binds
    abstract fun bindAlternativeSourceRemoteDataSource(
        alternativeSourceRemoteDataSourceImpl: AlternativeSourceRemoteDataSourceImpl
    ): AlternativeSourceRemoteDataSource

    @Binds
    abstract fun bindMonsterFolderLocalDataSource(
        monsterFolderLocalDataSourceImpl: MonsterFolderLocalDataSourceImpl
    ): MonsterFolderLocalDataSource
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

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

    @Singleton
    @Binds
    abstract fun bindAlternativeSourceRepository(
        alternativeSourceRepositoryImpl: AlternativeSourceRepositoryImpl
    ): AlternativeSourceRepository
}
