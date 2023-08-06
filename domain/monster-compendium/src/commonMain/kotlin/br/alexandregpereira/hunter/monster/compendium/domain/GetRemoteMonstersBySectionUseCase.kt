package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.domain.usecase.GetMonsterImagesUseCase
import br.alexandregpereira.hunter.domain.usecase.GetRemoteMonstersBySourceUseCase
import br.alexandregpereira.hunter.domain.usecase.appendMonsterImages
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class GetRemoteMonstersBySectionUseCase internal constructor(
    private val getRemoteMonstersBySourceUseCase: GetRemoteMonstersBySourceUseCase,
    private val getMonstersBySectionUseCase: GetMonstersBySectionUseCase,
    private val getMonsterImagesUseCase: GetMonsterImagesUseCase,
) {

    operator fun invoke(sourceAcronym: String): Flow<List<MonsterCompendiumItem>> {
        return getMonstersBySectionUseCase(
            flow {
                val monsters = coroutineScope {
                    val monstersDeferred = async {
                        getRemoteMonstersBySourceUseCase(sourceAcronym).single()
                    }

                    val imagesDeferred = async {
                        getMonsterImagesUseCase().single()
                    }

                    monstersDeferred.await().appendMonsterImages(imagesDeferred.await())
                }
                emit(monsters)
            }
        )
    }
}