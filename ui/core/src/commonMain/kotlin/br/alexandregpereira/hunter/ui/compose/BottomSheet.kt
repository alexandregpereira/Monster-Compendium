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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    opened: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val backgroundColor: Color = MaterialTheme.colors.background.copy(alpha = 0.7f)
    Closeable(
        opened = opened,
        backgroundColor = backgroundColor,
        onClosed = onClose,
    )

    SwipeVerticalToDismiss(
        visible = opened,
        onClose = onClose
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
                    .align(Alignment.BottomCenter)
                    .noIndicationClick(onClick = onClose),
                horizontalAlignment = Alignment.End,
            ) {
                val topSpaceHeight = 288.dp
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topSpaceHeight + contentPadding.calculateTopPadding())
                )
                Window(
                    modifier = Modifier.widthIn(max = 720.dp)
                        .fillMaxWidth()
                        .noIndicationClick(),
                ) {
                    Column(
                        modifier = modifier.padding(
                            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                        )
                    ) {
                        content()
                        Spacer(modifier = Modifier.height(contentPadding.calculateBottomPadding()))
                    }
                }
            }
        }
    }
}
