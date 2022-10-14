package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.settings.DEFAULT_API_BASE_URL
import br.alexandregpereira.hunter.data.settings.network.AlternativeSourceBaseUrlInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        alternativeSourceBaseUrlInterceptor: AlternativeSourceBaseUrlInterceptor
    ): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(alternativeSourceBaseUrlInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(DEFAULT_API_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
