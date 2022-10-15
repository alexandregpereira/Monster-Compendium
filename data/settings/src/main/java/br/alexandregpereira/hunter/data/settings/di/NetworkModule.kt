package br.alexandregpereira.hunter.data.settings.di

import br.alexandregpereira.hunter.data.settings.network.AlternativeSourceBaseUrlInterceptor
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceBaseUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetImageBaseUrlUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    fun provideAlternativeSourceBaseUrlInterceptor(
        getAlternativeSourceBaseUrlUseCase: GetAlternativeSourceBaseUrlUseCase,
        getImageBaseUrlUseCase: GetImageBaseUrlUseCase
    ): AlternativeSourceBaseUrlInterceptor {
        return AlternativeSourceBaseUrlInterceptor(
            getAlternativeSourceBaseUrlUseCase,
            getImageBaseUrlUseCase
        )
    }
}
