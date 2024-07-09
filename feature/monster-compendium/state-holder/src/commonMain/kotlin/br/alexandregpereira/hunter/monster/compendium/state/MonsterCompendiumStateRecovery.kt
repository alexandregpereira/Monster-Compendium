package br.alexandregpereira.hunter.monster.compendium.state

import br.alexandregpereira.hunter.ui.StateRecovery

internal val Map<String, Any?>.isShowingMonsterFolderPreview: Boolean
    get() = this["monsterCompendium:isShowingMonsterFolderPreview"] as? Boolean ?: false

internal fun MonsterCompendiumState.saveState(stateRecovery: StateRecovery): MonsterCompendiumState {
    stateRecovery["monsterCompendium:isShowingMonsterFolderPreview"] = isShowingMonsterFolderPreview
    stateRecovery.dispatchChanges()
    return this
}

internal fun MonsterCompendiumState.updateState(bundle: Map<String, Any?>): MonsterCompendiumState {
    return copy(
        isShowingMonsterFolderPreview = bundle.isShowingMonsterFolderPreview,
    )
}
