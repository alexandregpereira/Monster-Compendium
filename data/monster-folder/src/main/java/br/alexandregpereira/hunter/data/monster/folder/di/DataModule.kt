/*
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

package br.alexandregpereira.hunter.data.monster.folder.di

import br.alexandregpereira.hunter.domain.folder.MonsterFolderRepository
import br.alexandregpereira.hunter.data.monster.folder.MonsterFolderRepositoryImpl
import br.alexandregpereira.hunter.data.monster.folder.local.MonsterFolderLocalDataSource
import br.alexandregpereira.hunter.data.monster.folder.local.MonsterFolderLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class DataModule {

    @ViewModelScoped
    @Binds
    internal abstract fun bindMonsterFolderRepository(
        monsterFolderRepository: MonsterFolderRepositoryImpl
    ): MonsterFolderRepository

    @Binds
    abstract fun bindMonsterFolderLocalDataSource(
        monsterFolderLocalDataSourceImpl: MonsterFolderLocalDataSourceImpl
    ): MonsterFolderLocalDataSource
}
