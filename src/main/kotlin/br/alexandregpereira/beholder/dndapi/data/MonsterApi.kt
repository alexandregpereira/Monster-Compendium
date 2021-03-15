package br.alexandregpereira.beholder.dndapi.data

import retrofit2.http.GET
import retrofit2.http.Path

interface MonsterApi {

    @GET("monsters/{monsterIndex}")
    suspend fun getMonster(@Path("monsterIndex") monsterIndex: String): Monster
}
