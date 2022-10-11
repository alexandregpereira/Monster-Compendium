package br.alexandregpereira.hunter.detail.domain.model

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster

data class MonsterDetail(
    val monsterIndexSelected: Int,
    val measurementUnit: MeasurementUnit,
    val monsters: List<Monster>
)
