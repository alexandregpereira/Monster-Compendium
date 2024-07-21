package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationAction
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationState
import br.alexandregpereira.hunter.state.ActionHandler
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MonsterRegistrationScreen(
    state: MonsterRegistrationState,
    actionHandler: ActionHandler<MonsterRegistrationAction>,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) = AppFullScreen(isOpen = state.isOpen, contentPadding, onClose = intent::onClose) {
    CompositionLocalProvider(LocalStrings provides state.strings) {
        val lazyListState = rememberLazyListState()

        LaunchedEffect(actionHandler.action) {
            actionHandler.action.collectLatest { action ->
                when (action) {
                    is MonsterRegistrationAction.GoToListPosition -> {
                        lazyListState.animateScrollToItem(action.position)
                    }
                }
            }
        }

        MonsterRegistrationForm(
            monster = state.monster,
            lazyListState = lazyListState,
            isSaveButtonEnabled = state.isSaveButtonEnabled,
            tableContent = state.tableContent,
            isTableContentOpen = state.isTableContentOpen,
            modifier = Modifier,
            contentPadding = contentPadding,
            intent = intent,
        )
    }
}
