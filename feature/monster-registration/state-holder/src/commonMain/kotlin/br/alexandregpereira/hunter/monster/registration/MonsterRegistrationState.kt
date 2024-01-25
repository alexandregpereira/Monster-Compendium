package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.domain.model.Monster

data class MonsterRegistrationState(
    val isLoading: Boolean = true,
    val isOpen: Boolean = false,
    val monster: Monster = Monster(index = ""),
    val initialSelectedStepIndex: Int = 0,
    val isSaveButtonEnabled: Boolean = false,
)
