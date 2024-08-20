/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.shareContent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent.Export
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexadregpereira.hunter.shareContent.event.exportEvents
import br.alexandregpereira.hunter.shareContent.state.ShareContentStateHolder
import br.alexandregpereira.hunter.shareContent.state.ShareContentUiEvent
import br.alexandregpereira.hunter.shareContent.ui.ShareContentExportScreen
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import org.koin.compose.koinInject

@Composable
fun ShareContentExportMonsterFeature(
    contentPadding: PaddingValues,
) {
    val eventDispatcher = koinInject<ShareContentEventDispatcher>()
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var monsterIndexes: List<String> by rememberSaveable {
        mutableStateOf(emptyList())
    }
    LaunchedEffect(eventDispatcher.events) {
        eventDispatcher.exportEvents().collect { event ->
            isOpen = when (event) {
                is Export.OnStart -> {
                    monsterIndexes = event.monsterIndexes
                    true
                }
                is Export.OnFinish -> false
            }
        }
    }
    BottomSheet(
        contentPadding = PaddingValues(
            end = 16.dp,
            start = 16.dp,
            bottom = 16.dp + contentPadding.calculateBottomPadding(),
        ),
        opened = isOpen,
        onClose = { isOpen = false },
    ) {
        val stateHolder = koinInject<ShareContentStateHolder>()
        val clipboardManager = LocalClipboardManager.current
        LaunchedEffect(monsterIndexes) {
            stateHolder.fetchMonsterContentToExport(
                monsterIndexes = monsterIndexes,
                actualClipboardContent = clipboardManager.getText()?.text
            )
        }

        val state by stateHolder.state.collectAsState()
        ShareContentExportScreen(
            state = state,
            onCopy = stateHolder::onCopyContentToExport,
        )

        LaunchedEffect(stateHolder.action) {
            stateHolder.action.collect { action ->
                when (action) {
                    is ShareContentUiEvent.CopyContentUiToExport -> {
                        clipboardManager.setText(AnnotatedString(action.content))
                    }
                }
            }
        }
    }
}
