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

package br.alexandregpereira.hunter.monster.lore.detail.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailState
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun MonsterLoreDetailScreen(
    state: MonsterLoreDetailState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {}
) = HunterTheme {
    BottomSheet(opened = state.showDetail, contentPadding = contentPadding, onClose = onClose) {
        state.monsterLore?.let { MonsterLoreDetail(it) }
    }
}
