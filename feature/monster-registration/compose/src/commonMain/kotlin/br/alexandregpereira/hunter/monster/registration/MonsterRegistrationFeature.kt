package br.alexandregpereira.hunter.monster.registration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.monster.registration.ui.MonsterRegistrationScreen
import org.koin.compose.koinInject

@Composable
fun MonsterRegistrationFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: MonsterRegistrationStateHolder = koinInject()
    MonsterRegistrationScreen(
        state = viewModel.state.collectAsState().value,
        actionHandler = viewModel,
        contentPadding = contentPadding,
        intent = viewModel,
    )
}
