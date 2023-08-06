package br.alexandregpereira.hunter.monster.content.preview.di

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewState
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewStateRecovery
import org.koin.core.scope.Scope

internal actual fun Scope.createStateRecovery(): MonsterContentPreviewStateRecovery? {
    return MonsterContentPreviewAndroidStateRecovery(get())
}

private class MonsterContentPreviewAndroidStateRecovery(
    private val savedStateHandle: SavedStateHandle
) : MonsterContentPreviewStateRecovery {

    override var sourceAcronym: String
        get() = savedStateHandle["sourceAcronym"] ?: ""
        set(value) = savedStateHandle.set("sourceAcronym", value)

    override var title: String
        get() = savedStateHandle["title"] ?: ""
        set(value) = savedStateHandle.set("title", value)

    override fun getState(): MonsterContentPreviewState {
        return MonsterContentPreviewState(
            isOpen = savedStateHandle["isOpen"] ?: false,
        )
    }

    override fun saveState(state: MonsterContentPreviewState) {
        savedStateHandle["isOpen"] = state.isOpen
    }
}
