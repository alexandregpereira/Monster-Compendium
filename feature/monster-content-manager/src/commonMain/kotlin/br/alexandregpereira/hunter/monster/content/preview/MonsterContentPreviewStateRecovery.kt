package br.alexandregpereira.hunter.monster.content.preview

import br.alexandregpereira.hunter.state.StateRecovery

interface MonsterContentPreviewStateRecovery : StateRecovery<MonsterContentPreviewState> {

    var sourceAcronym: String

    var title: String
}

internal class MonsterContentPreviewDefaultStateRecovery : MonsterContentPreviewStateRecovery {

    private var state: MonsterContentPreviewState = MonsterContentPreviewState()

    override var sourceAcronym: String = ""

    override var title: String = ""

    override fun getState(): MonsterContentPreviewState = state

    override fun saveState(state: MonsterContentPreviewState) {
        this.state = state
    }
}
