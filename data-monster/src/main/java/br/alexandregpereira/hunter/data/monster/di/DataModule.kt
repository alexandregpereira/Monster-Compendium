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

package br.alexandregpereira.hunter.data.monster.di

import br.alexandregpereira.hunter.data.monster.MonsterRepositoryImpl
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSourceImpl
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSourceImpl
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindMonsterRemoteDataSource(
        monsterRemoteDataSourceImpl: MonsterRemoteDataSourceImpl
    ): MonsterRemoteDataSource

    @Binds
    abstract fun bindMonsterLocalDataSource(
        monsterLocalDataSourceImpl: MonsterLocalDataSourceImpl
    ): MonsterLocalDataSource

    @Singleton
    @Binds
    abstract fun bindMonsterRepository(
        monsterRepositoryImpl: MonsterRepositoryImpl
    ): MonsterRepository
}
