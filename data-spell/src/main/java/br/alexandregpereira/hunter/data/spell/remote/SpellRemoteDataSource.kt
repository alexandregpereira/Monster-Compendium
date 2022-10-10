package br.alexandregpereira.hunter.data.spell.remote

import br.alexandregpereira.hunter.data.spell.remote.model.SpellDto
import kotlinx.coroutines.flow.Flow

internal interface SpellRemoteDataSource {

    fun getSpells(): Flow<List<SpellDto>>
}
