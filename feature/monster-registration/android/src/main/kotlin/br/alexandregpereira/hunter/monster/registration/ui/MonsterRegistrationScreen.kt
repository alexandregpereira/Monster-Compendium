package br.alexandregpereira.hunter.monster.registration.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.getFakeMonster
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationState
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterRegistrationScreen(
    state: MonsterRegistrationState,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) {
    BackHandler(enabled = state.isOpen, onBack = intent::onClose)

    SwipeVerticalToDismiss(visible = state.isOpen, onClose = intent::onClose) {
        Window(
            Modifier.fillMaxSize()
        ) {
            MonsterRegistrationForm(
                monster = state.monster,
                modifier = Modifier,
                contentPadding = contentPadding,
                intent = intent,
            )
        }
    }
}

@Preview
@Composable
private fun MonsterRegistrationScreenPreview() {
    val state = remember {
        mutableStateOf(
            MonsterRegistrationState(
                isOpen = true,
                monster = getFakeMonster(),
            )
        )
    }
    val intent = object : MonsterRegistrationIntent {
        override fun onClose() {}

        override fun onMonsterChanged(monster: Monster) {
            state.value = state.value.copy(monster = monster)
        }

        override fun onSaved() {}

        override fun onSpellClick(spellIndex: String) {}
    }
    MonsterRegistrationScreen(
        state = state.value,
        intent = intent,
    )
}
