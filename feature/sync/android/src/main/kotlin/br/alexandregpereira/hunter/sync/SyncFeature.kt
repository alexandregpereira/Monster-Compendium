package br.alexandregpereira.hunter.sync

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.sync.ui.SyncScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SyncFeature() {
    val viewModel: SyncViewModel = koinViewModel()

    SyncScreen(
        state = viewModel.state.collectAsState().value,
        onTryAgain = viewModel::onTryAgain
    )
}
