/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.ui.compose.tablecontent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState.BODY
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState.HEADER1
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState.HEADER2

@Composable
internal fun TableContent(
    tableContent: List<TableContentItemState>,
    selectedIndex: Int,
    onTableContentClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
) {
    LazyHorizontalGrid(
        state = rememberLazyGridState(initialFirstVisibleItemIndex = initialIndex),
        rows = GridCells.Adaptive(minSize = 60.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        itemsIndexed(tableContent, key = { _, content -> content.id }) { index, content ->
            val color = if (selectedIndex == index) {
                MaterialTheme.colors.background
            } else Color.Transparent

            Box(
                Modifier
                    .width(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color)
                    .clickable(
                        enabled = selectedIndex != index,
                        onClick = { onTableContentClicked(index) }
                    )
            ) {
                val style = when (content.type) {
                    HEADER1 -> TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    )
                    HEADER2 -> TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    BODY -> TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = content.text,
                    color = MaterialTheme.colors.onBackground,
                    style = style,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

data class TableContentItemState(
    val text: String,
    val type: TableContentItemTypeState,
    val id: String = text
)

enum class TableContentItemTypeState {
    HEADER1, HEADER2, BODY
}
