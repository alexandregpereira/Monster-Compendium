package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.ui.StateRecovery

internal val StateRecovery.monsterIndex: String
    get() = this["monsterDetail:monsterIndex"] as? String ?: ""

internal val StateRecovery.monsterIndexes: List<String>
    get() = (this["monsterDetail:monsterIndexes"] as? List<*>)?.map { it as String } ?: emptyList()

internal fun StateRecovery.saveMonsterIndex(monsterIndex: String) {
    this["monsterDetail:monsterIndex"] = monsterIndex
    dispatchChanges()
}

internal fun StateRecovery.saveMonsterIndexes(monsterIndexes: List<String>) {
    this["monsterDetail:monsterIndexes"] = monsterIndexes
    dispatchChanges()
}

internal fun MonsterDetailState.saveState(stateRecovery: StateRecovery): MonsterDetailState {
    stateRecovery["monsterDetail:showDetail"] = showDetail
    stateRecovery["monsterDetail:showCloneForm"] = showCloneForm
    stateRecovery["monsterDetail:monsterCloneName"] = monsterCloneName
    stateRecovery.dispatchChanges()
    return this
}

internal fun MonsterDetailState.updateState(bundle: Map<String, Any?>): MonsterDetailState {
    return copy(
        showDetail = bundle["monsterDetail:showDetail"] as? Boolean ?: false,
        showCloneForm = bundle["monsterDetail:showCloneForm"] as? Boolean ?: false,
        monsterCloneName = bundle["monsterDetail:monsterCloneName"] as? String ?: ""
    )
}
