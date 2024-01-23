package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.domain.model.Monster

interface MonsterRegistrationIntent {

    fun onClose()

    fun onMonsterChanged(monster: Monster)

    fun onSaved()

    fun onSpellClick(spellIndex: String)
}

class EmptyMonsterRegistrationIntent : MonsterRegistrationIntent {

    override fun onClose() {}

    override fun onMonsterChanged(monster: Monster) {}

    override fun onSaved() {}

    override fun onSpellClick(spellIndex: String) {}
}
