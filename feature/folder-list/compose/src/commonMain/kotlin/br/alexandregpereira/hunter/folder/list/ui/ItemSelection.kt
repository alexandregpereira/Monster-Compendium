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

package br.alexandregpereira.hunter.folder.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ItemSelection(
    itemSelectionText: String,
    deleteText: String,
    addToPreviewText: String,
    modifier: Modifier = Modifier,
    isOpen: Boolean = true,
    contentBottomPadding: Dp = 0.dp,
    onClose: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onAddToPreviewClick: () -> Unit = {},
) = BottomSheet(
    opened = isOpen,
    maxWidth = Dp.Unspecified,
    closeClickingOutside = false,
    onClose = onClose,
) {
    AppCard(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            ScreenHeader(
                title = itemSelectionText,
            )

            AppButton(
                text = addToPreviewText,
                modifier = Modifier.padding(top = 24.dp),
                isPrimary = false,
                elevation = 4,
                onClick = onAddToPreviewClick
            )

            AppButton(
                text = deleteText,
                modifier = Modifier.padding(top = 16.dp),
                onClick = onDeleteClick
            )

            Spacer(modifier = Modifier.height(contentBottomPadding))
        }
    }
}

@Preview
@Composable
private fun ItemSelectionPreview() = HunterTheme {
    Box(Modifier.fillMaxSize()) {
        ItemSelection(
            itemSelectionText = "Item Selection",
            deleteText = "Delete",
            addToPreviewText = "Add to Preview",
        )
    }
}
