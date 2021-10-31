package br.alexandregpereira.hunter.monster.compendium

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object MonsterCompendiumModule {

    @LoadOnInitFlag
    @Provides
    fun provideLoadOnInit(): Boolean = true
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoadOnInitFlag
