package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.settings.network.AlternativeSourceUrlBuilder
import br.alexandregpereira.hunter.network.NetworkManager
import br.alexandregpereira.hunter.network.NetworkManagerImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.plugin
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url("https://raw.githubusercontent.com/alexandregpereira/Monster-Compendium-Content/main/json/")
            }
            install(Logging) {
                logger = Logger.SIMPLE
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                val newRequestBuilder = get<AlternativeSourceUrlBuilder>().execute(request)
                execute(newRequestBuilder)
            }
        }
    }

    single { Json { ignoreUnknownKeys = true } }

    factory<NetworkManager> {
        NetworkManagerImpl(
            client = get(),
        )
    }
}
