package br.alexandregpereira.hunter.sync

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.alexandregpereira.hunter.sync.ui.SyncScreen
import org.koin.compose.koinInject

@Composable
fun SyncFeature() {
    val viewModel: SyncStateHolder = koinInject()

    SyncScreen(
        state = viewModel.state.collectAsState().value,
        onTryAgain = viewModel::onTryAgain
    )
}
