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

package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Suppress("FunctionName")
internal fun LazyListScope.FormLazy(
    titleKey: String,
    title: @Composable () -> String,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    formItem(key = titleKey) {
        Column(modifier) {
            Spacer(modifier = Modifier.height(16.dp))
            ScreenHeader(
                title = title(),
            )
        }
    }

    content()
}

internal fun LazyListScope.formItem(
    key: String,
    modifier: Modifier = Modifier,
    content: @Composable LazyItemScope.() -> Unit
) = item(key) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .animateItem()
    ) {
        content()
    }
}
