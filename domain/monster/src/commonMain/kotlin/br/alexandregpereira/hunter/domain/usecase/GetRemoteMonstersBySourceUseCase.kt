package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
import br.alexandregpereira.hunter.domain.sort.sortMonstersByNameAndGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetRemoteMonstersBySourceUseCase internal constructor(
    private val repository: MonsterRemoteRepository,
    private val monsterSettingsRepository: MonsterSettingsRepository,
) {

    operator fun invoke(sourceAcronym: String): Flow<List<Monster>> {
        return monsterSettingsRepository.getLanguage().map {  lang: String ->
            repository.getMonsters(
                sourceAcronym = sourceAcronym,
                lang = lang
            ).single()
                .distinctBy { it.index }
                .sortMonstersByNameAndGroup()
        }
    }
}
