package br.alexandregpereira.hunter.monster.registration.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
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
        Window(Modifier.fillMaxSize()) {
            CompositionLocalProvider(LocalStrings provides state.strings) {
                MonsterRegistrationForm(
                    monster = state.monster,
                    isSaveButtonEnabled = state.isSaveButtonEnabled,
                    modifier = Modifier,
                    contentPadding = contentPadding,
                    intent = intent,
                )
            }
        }
    }
}
