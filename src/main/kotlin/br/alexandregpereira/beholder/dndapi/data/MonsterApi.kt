package br.alexandregpereira.beholder.dndapi.data

import retrofit2.http.GET
import retrofit2.http.Path

interface MonsterApi {

    @GET("monsters")
    suspend fun getMonsters(): Response

    @GET("monsters/{monsterIndex}")
    suspend fun getMonster(@Path("monsterIndex") monsterIndex: String): Monster
}
