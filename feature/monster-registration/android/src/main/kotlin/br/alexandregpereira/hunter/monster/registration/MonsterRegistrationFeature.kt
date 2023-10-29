package br.alexandregpereira.hunter.monster.registration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.monster.registration.ui.MonsterRegistrationScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MonsterRegistrationFeature(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: MonsterRegistrationViewModel = koinViewModel()
    MonsterRegistrationScreen(
        state = viewModel.state.collectAsState().value,
        contentPadding = contentPadding,
        intent = viewModel,
    )
}