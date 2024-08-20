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

package br.alexandregpereira.hunter.ui.compose.tablecontent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState.BODY
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Preview(widthDp = 320, heightDp = 480)
@Composable
private fun TableContentPopupNotOpenedPreview() = HunterTheme {
    var opened by remember { mutableStateOf(false) }
    var tableContentOpened by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        TableContentPopup(
            alphabet = (0..15).map { it.toString() },
            tableContent = (0..16).map { it.toString() + "asdasd asdas adadsasd adasd ads d" }
                .map { TableContentItemState(it, BODY) },
            tableContentSelectedIndex = 0,
            alphabetSelectedIndex = 0,
            opened = opened,
            tableContentOpened = tableContentOpened,
            onOpenButtonClicked = { opened = true },
            onCloseButtonClicked = { opened = false },
            onAlphabetIndexClicked = { tableContentOpened = true },
            onTableContentClicked = { tableContentOpened = false },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@Preview(widthDp = 400, heightDp = 640)
@Composable
private fun TableContentPopupOpenedPreview() = HunterTheme {
    var opened by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        TableContentPopup(
            alphabet = (0..15).map { it.toString() },
            tableContent = (0..16)
                .map { it.toString() + "asdasd asdas adadsasd adasd ads d" }
                .map { TableContentItemState(it, BODY) },
            tableContentSelectedIndex = 0,
            alphabetSelectedIndex = 0,
            opened = true,
            tableContentOpened = opened,
            onOpenButtonClicked = {},
            onCloseButtonClicked = { opened = false },
            onTableContentClicked = { opened = false },
            onAlphabetIndexClicked = { opened = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@Preview(widthDp = 400, heightDp = 640)
@Composable
private fun PopupOpenedContentClosedPreview() = HunterTheme {
    Box(Modifier.fillMaxSize()) {
        TableContentPopup(
            alphabet = (0..15).map { it.toString() },
            tableContent = (0..16)
                .map { it.toString() + "asdasd asdas adadsasd adasd ads d" }
                .map { TableContentItemState(it, BODY) },
            tableContentSelectedIndex = 0,
            alphabetSelectedIndex = 0,
            opened = true,
            tableContentOpened = true,
            onOpenButtonClicked = {},
            onCloseButtonClicked = {},
            onTableContentClicked = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}
