package br.alexandregpereira.hunter.data.settings.network

import br.alexandregpereira.hunter.data.settings.DEFAULT_API_BASE_URL
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceBaseUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetImageBaseUrlUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AlternativeSourceBaseUrlInterceptor(
    private val getAlternativeSourceBaseUrl: GetAlternativeSourceBaseUrlUseCase,
    private val getImageBaseUrl: GetImageBaseUrlUseCase
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val currentUrl = request.url.toString()
        val newRequest = currentUrl.takeIf { url ->
            isToInterceptUrl(url)
        }?.let { url ->
            getNewUrl(url)
        }?.let { newUrl ->
            request.newBuilder().url(newUrl).build()
        }

        return chain.proceed(newRequest ?: request)
    }

    private fun isToInterceptUrl(url: String): Boolean {
        return when {
            url.contains("monster-images.json") -> true
            url.contains("alternative-sources.json") -> true
            url.contains("/sources/") -> true
            else -> false
        }
    }

    private fun getNewUrl(currentUrl: String): String? = runBlocking {
        when {
            currentUrl.contains("monster-images.json") -> getImageBaseUrl()
            currentUrl.contains("alternative-sources.json") -> getAlternativeSourceBaseUrl()
            currentUrl.contains("/sources/") -> getAlternativeSourceBaseUrl().map { url ->
                currentUrl.replace(
                    DEFAULT_API_BASE_URL,
                    url.replace("alternative-sources.json", "")
                )
            }
            else -> null
        }?.map { url ->
            url.ifBlank {
                "https://localhost/"
            }
        }?.single()
    }
}
