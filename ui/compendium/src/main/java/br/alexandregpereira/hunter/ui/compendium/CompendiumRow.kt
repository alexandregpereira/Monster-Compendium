/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compendium

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <CardState> CompendiumRow(
    leftCard: CardState,
    modifier: Modifier = Modifier,
    rightCard: CardState? = null,
    content: @Composable (CardState) -> Unit,
) = Row(modifier) {

    Box(
        modifier = Modifier
            .weight(1f)
            .padding(end = if (rightCard != null) 8.dp else 0.dp)
    ) {
        content(leftCard)
    }

    if (rightCard != null) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            content(rightCard)
        }
    }
}
