package br.alexandregpereira.hunter.monster.registration

interface MonsterRegistrationIntent {

    fun onClose()

    fun onMonsterChanged(monster: MonsterState)

    fun onSaved()

    fun onSpellClick(spellIndex: String)
}

class EmptyMonsterRegistrationIntent : MonsterRegistrationIntent {

    override fun onClose() {}

    override fun onMonsterChanged(monster: MonsterState) {}

    override fun onSaved() {}

    override fun onSpellClick(spellIndex: String) {}
}
