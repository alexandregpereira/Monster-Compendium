package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationAction
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationState
import br.alexandregpereira.hunter.state.ActionHandler
import br.alexandregpereira.hunter.ui.compose.AppScreen
import br.alexandregpereira.hunter.ui.compose.ClearFocusWhenScrolling
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.isLandscape
import br.alexandregpereira.hunter.ui.compose.maxBottomSheetWidth
import br.alexandregpereira.hunter.ui.compose.plus
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MonsterRegistrationScreen(
    state: MonsterRegistrationState,
    actionHandler: ActionHandler<MonsterRegistrationAction>,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) = Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
    val screenSize = LocalScreenSize.current
    AppScreen(
        isOpen = state.isOpen,
        contentPaddingValues = contentPadding,
        modifier = Modifier.widthIn(
            max = maxBottomSheetWidth.takeIf { screenSize.isLandscape } ?: Dp.Unspecified
        ),
        onClose = intent::onClose
    ) {
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

            ClearFocusWhenScrolling(lazyListState)

            MonsterRegistrationForm(
                monster = state.monster,
                lazyListState = lazyListState,
                isSaveButtonEnabled = state.isSaveButtonEnabled,
                tableContent = state.tableContent,
                isTableContentOpen = state.isTableContentOpen,
                modifier = Modifier,
                contentPadding = contentPadding + PaddingValues(top = 24.dp),
                intent = intent,
            )
        }
    }
}
