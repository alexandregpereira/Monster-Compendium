package br.alexandregpereira.hunter.data.spell.di

import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSourceImpl
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSourceImpl
import br.alexandregpereira.hunter.data.spell.SpellRepositoryImpl
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
}
