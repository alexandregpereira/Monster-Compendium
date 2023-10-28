package br.alexandregpereira.hunter.monster.registration.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationState
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterRegistrationScreen(
    state: MonsterRegistrationState,
    contentPadding: PaddingValues = PaddingValues(),
    onMonsterChanged: (Monster) -> Unit = {},
    onClose: () -> Unit = {},
) {
    BackHandler(enabled = state.isOpen, onBack = onClose)

    SwipeVerticalToDismiss(visible = state.isOpen, onClose = onClose) {
        Window(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Form(
                title = "Test",
                formFields = listOf(
                    FormField(
                        key = "monsterName",
                        label = "Name",
                        value = state.monster.name,
                    ),
                    FormField(
                        key = "group",
                        label = "Group",
                        value = state.monster.group.orEmpty(),
                    ),
                ),
                onFormChanged = { field ->
                    if (field.key == "monsterName") {
                        onMonsterChanged(state.monster.copy(name = field.value))
                    }
                },
            )
        }
    }

}