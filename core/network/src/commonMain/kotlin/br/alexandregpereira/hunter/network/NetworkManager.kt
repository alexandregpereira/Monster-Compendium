package br.alexandregpereira.hunter.network

interface NetworkManager {
    suspend fun isNetworkAvailable(): Boolean
}
