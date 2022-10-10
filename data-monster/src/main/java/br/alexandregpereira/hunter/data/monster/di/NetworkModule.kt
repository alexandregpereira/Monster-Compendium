package br.alexandregpereira.hunter.data.monster.di

import br.alexandregpereira.hunter.data.monster.remote.MonsterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    fun provideMonsterApi(retrofit: Retrofit): MonsterApi {
        return retrofit.create(MonsterApi::class.java)
    }
}
