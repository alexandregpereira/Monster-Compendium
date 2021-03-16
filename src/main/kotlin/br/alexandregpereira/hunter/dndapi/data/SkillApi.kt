package br.alexandregpereira.hunter.dndapi.data

import retrofit2.http.GET
import retrofit2.http.Path

interface SkillApi {

    @GET("skills")
    suspend fun getSkills(): Response

    @GET("skills/{index}")
    suspend fun getSkill(@Path("index") index: String): Skill
}