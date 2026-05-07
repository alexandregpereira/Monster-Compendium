package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.MonsterImage

fun interface SaveMonsterImages {
    suspend operator fun invoke(vararg monsterImages: MonsterImage)
}
