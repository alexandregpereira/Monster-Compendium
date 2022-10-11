package br.alexandregpereira.hunter.data.spell.remote

import br.alexandregpereira.hunter.data.spell.remote.model.SpellDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SpellRemoteDataSourceImpl @Inject constructor(
    private val api: SpellApi
) : SpellRemoteDataSource {

    override fun getSpells(): Flow<List<SpellDto>> = flow {
        emit(api.getSpells())
    }
}
