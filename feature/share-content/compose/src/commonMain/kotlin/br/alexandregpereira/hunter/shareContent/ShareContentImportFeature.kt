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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent.Import
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexadregpereira.hunter.shareContent.event.importEvents
import br.alexandregpereira.hunter.shareContent.state.ShareContentStateHolder
import br.alexandregpereira.hunter.shareContent.ui.ShareContentImportScreen
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import org.koin.compose.koinInject

@Composable
fun ShareContentImportFeature(
    contentPadding: PaddingValues,
) {
    val eventDispatcher = koinInject<ShareContentEventDispatcher>()
    var isOpen by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(eventDispatcher.events) {
        eventDispatcher.importEvents().collect { event ->
            isOpen = when (event) {
                is Import.OnStart -> true
                is Import.OnFinish -> false
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
        modifier = Modifier.animateContentSize()
    ) {
        val stateHolder = koinInject<ShareContentStateHolder>()
        ShareContentImportScreen(
            state = stateHolder.state.collectAsState().value,
            onImport = stateHolder::onImport,
            onPaste = stateHolder::onPasteImportContent,
        )
    }
}
