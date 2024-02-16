package br.alexandregpereira.hunter.monster.registration

sealed class MonsterRegistrationAction {

    data class GoToListPosition(val position: Int) : MonsterRegistrationAction()
}
