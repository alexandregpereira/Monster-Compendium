package br.alexandregpereira.beholder.dndapi.data

import retrofit2.http.GET
import retrofit2.http.Path

interface DamageTypeApi {

    @GET("damage-types")
    suspend fun getDamageTypes(): Response

    @GET("damage-types/{index}")
    suspend fun getDamageType(@Path("index") index: String): DamageType
}