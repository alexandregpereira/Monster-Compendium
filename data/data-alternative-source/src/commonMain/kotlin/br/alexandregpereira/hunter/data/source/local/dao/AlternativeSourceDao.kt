package br.alexandregpereira.hunter.data.source.local.dao

import br.alexandregpereira.hunter.data.source.local.entity.AlternativeSourceEntity

interface AlternativeSourceDao {

    suspend fun getAlternativeSources(): List<AlternativeSourceEntity>

    suspend fun addAlternativeSource(alternativeSource: AlternativeSourceEntity)

    suspend fun removeAlternativeSource(acronym: String)
}