/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.spell.di

import br.alexandregpereira.hunter.data.spell.SettingsSpellDataRepositoryImpl
import br.alexandregpereira.hunter.data.spell.SpellRepositoryImpl
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSourceImpl
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSourceImpl
import br.alexandregpereira.hunter.domain.settings.SettingsSpellDataRepository
import br.alexandregpereira.hunter.domain.spell.SpellRepository
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
        spellRepositoryImpl: SpellRepositoryImpl
    ): SpellRepository

    @Binds
    internal abstract fun bindSpellLocalDataSource(
        spellLocalDataSourceImpl: SpellLocalDataSourceImpl
    ): SpellLocalDataSource

    @Binds
    internal abstract fun bindSpellRemoteDataSource(
        spellRemoteDataSourceImpl: SpellRemoteDataSourceImpl
    ): SpellRemoteDataSource

    @Binds
    internal abstract fun bindSettingsSpellDataRepository(
        repository: SettingsSpellDataRepositoryImpl
    ): SettingsSpellDataRepository
}
