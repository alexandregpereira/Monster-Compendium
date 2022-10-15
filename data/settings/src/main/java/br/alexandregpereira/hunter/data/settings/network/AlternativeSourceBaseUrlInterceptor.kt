package br.alexandregpereira.hunter.data.settings.network

import br.alexandregpereira.hunter.data.settings.DEFAULT_API_BASE_URL
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceBaseUrlUseCase
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class AlternativeSourceBaseUrlInterceptor(
    private val getAlternativeSourceBaseUrl: GetAlternativeSourceBaseUrlUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val currentUrl = request.url.toString()
        val newRequest = currentUrl.takeIf { it.contains("sources") }
            ?.let {
                runBlocking { getAlternativeSourceBaseUrl().single() }
                    .takeIf { baseUrl -> baseUrl.isNotBlank() }
            }
            ?.let { newBaseUrl ->
                request.newBuilder()
                    .url(
                        currentUrl
                            .replace(DEFAULT_API_BASE_URL, newBaseUrl)
                            .toHttpUrlOrNull() ?: request.url
                    )
                    .build()
            }

        return chain.proceed(newRequest ?: request)
    }
}
