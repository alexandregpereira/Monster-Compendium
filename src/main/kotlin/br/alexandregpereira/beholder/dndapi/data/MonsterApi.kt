package br.alexandregpereira.beholder.dndapi.data

import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


interface MonsterApi {

    @GET("monsters")
    suspend fun getMonsters(): MonsterResponse

    @GET("monsters/{monsterIndex}")
    suspend fun getMonster(@Path("monsterIndex") monsterIndex: String): Monster
}
