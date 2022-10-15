package br.alexandregpereira.hunter.data.source.di

import br.alexandregpereira.hunter.data.source.remote.AlternativeSourceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    fun provideAlternativeSourceApi(retrofit: Retrofit): AlternativeSourceApi {
        return retrofit.create(AlternativeSourceApi::class.java)
    }
}
