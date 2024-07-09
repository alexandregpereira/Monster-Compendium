package br.alexandregpereira.hunter.monster.content.preview

import br.alexandregpereira.hunter.ui.StateRecovery

internal fun StateRecovery.getState(): MonsterContentPreviewState {
    return MonsterContentPreviewState(
        title = this.title,
        isOpen = this["monsterContentPreview:isOpen"] as? Boolean ?: false,
    )
}

internal fun MonsterContentPreviewState.saveState(
    stateRecovery: StateRecovery
): MonsterContentPreviewState {
    stateRecovery["monsterContentPreview:title"] = title
    stateRecovery["monsterContentPreview:isOpen"] = isOpen
    stateRecovery.dispatchChanges()
    return this
}

internal val StateRecovery.title: String
    get() = this["monsterContentPreview:title"] as? String ?: ""

internal var StateRecovery.sourceAcronym: String
    get() = this["monsterContentPreview:sourceAcronym"] as? String ?: ""
    set(value) {
        this["monsterContentPreview:sourceAcronym"] = value
        dispatchChanges()
    }
