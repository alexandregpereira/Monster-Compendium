package br.alexandregpereira.hunter.data.spell.di

import br.alexandregpereira.hunter.data.spell.remote.SpellApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Singleton
    @Provides
    fun provideSpellApi(retrofit: Retrofit): SpellApi {
        return retrofit.create(SpellApi::class.java)
    }
}
