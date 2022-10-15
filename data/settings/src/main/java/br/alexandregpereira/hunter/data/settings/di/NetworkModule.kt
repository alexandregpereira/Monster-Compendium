package br.alexandregpereira.hunter.data.settings.di

import br.alexandregpereira.hunter.data.settings.network.AlternativeSourceBaseUrlInterceptor
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceBaseUrlUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    fun provideAlternativeSourceBaseUrlInterceptor(
        getAlternativeSourceBaseUrlUseCase: GetAlternativeSourceBaseUrlUseCase
    ): AlternativeSourceBaseUrlInterceptor {
        return AlternativeSourceBaseUrlInterceptor(getAlternativeSourceBaseUrlUseCase)
    }
}
