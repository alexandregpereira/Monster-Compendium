package br.alexandregpereira.hunter.monster.registration

interface MonsterRegistrationIntent {

    fun onClose()

    fun onMonsterChanged(monster: MonsterState)

    fun onSaved()

    fun onSpellClick(spellIndex: String)

    fun onTableContentClick(key: String)

    fun onTableContentClose()

    fun onTableContentOpen()
}

class EmptyMonsterRegistrationIntent : MonsterRegistrationIntent {

    override fun onClose() {}

    override fun onMonsterChanged(monster: MonsterState) {}

    override fun onSaved() {}

    override fun onSpellClick(spellIndex: String) {}

    override fun onTableContentClick(key: String) {}

    override fun onTableContentClose() {}

    override fun onTableContentOpen() {}
}
