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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.compendium.ui.TableContentItemTypeState.BODY
import br.alexandregpereira.hunter.ui.compose.noIndicationClick
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun TableContentPopup(
    alphabet: List<String>,
    alphabetSelectedIndex: Int,
    tableContent: List<TableContentItemState>,
    tableContentSelectedIndex: Int,
    opened: Boolean,
    onOpenButtonClicked: () -> Unit,
    onCloseButtonClicked: () -> Unit,
    onTableContentClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tableContentOpened: Boolean = false,
    tableContentInitialIndex: Int = 0,
    onAlphabetIndexClicked: (Int) -> Unit = {},
    onTableContentClosed: () -> Unit = {},
) {
    if (tableContent.isEmpty()) return
    val transition = updateTransition(targetState = opened, label = "TableContent")

    val percent by transition.animateInt(
        label = "animatePercent",
        transitionSpec = {
            val isOpen = this.initialState
            spring(stiffness = if (isOpen) Spring.StiffnessVeryLow else Spring.StiffnessMedium)
        }
    ) { isOpen ->
        if (isOpen) 10 else 100
    }

    val colorStiffnessIn = Spring.StiffnessMedium
    val colorStiffnessOut = Spring.StiffnessVeryLow
    val primaryColor by transition.animateColor(
        label = "primaryColor",
        transitionSpec = {
            val isOpen = this.targetState
            spring(stiffness = if (isOpen) colorStiffnessIn else colorStiffnessOut)
        }
    ) { isOpen ->
        if (isOpen) MaterialTheme.colors.surface else MaterialTheme.colors.secondary
    }
    val secondaryColor by transition.animateColor(
        label = "secondaryColor",
        transitionSpec = {
            val isOpen = this.targetState
            spring(stiffness = if (isOpen) colorStiffnessIn else colorStiffnessOut)
        }
    ) { isOpen ->
        if (isOpen) MaterialTheme.colors.background else MaterialTheme.colors.secondaryVariant
    }

    BackHandler(enabled = tableContentOpened, onBack = onTableContentClosed)

    Box(modifier) {
        Box(
            Modifier
                .wrapContentSize()
                .clip(shape = RoundedCornerShape(percent))
                .background(color = primaryColor)
                .noIndicationClick()
                .animateContentSize(
                    animationSpec = spring(Spring.DampingRatioLowBouncy)
                )
        ) {

            AnimatedVisibility(
                visible = opened.not(),
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Box(
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .clickableWithRippleEffect(onClick = onOpenButtonClicked)
                ) {
                    Text(
                        text = alphabet.getOrNull(alphabetSelectedIndex) ?: "",
                        color = MaterialTheme.colors.primaryVariant,
                        style = TextStyle(fontSize = 24.sp),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            AnimatedVisibility(
                visible = opened,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
                modifier = Modifier
                    .padding(bottom = 56.dp)
                    .animateContentSize(
                        animationSpec = spring(Spring.DampingRatioLowBouncy)
                    )
            ) {
                AnimatedVisibility(
                    visible = tableContentOpened,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
                    exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
                ) {
                    TableContent(
                        tableContent,
                        tableContentSelectedIndex,
                        onTableContentClicked,
                        initialIndex = tableContentInitialIndex
                    )
                }
                AnimatedVisibility(
                    visible = tableContentOpened.not(),
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
                    exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
                ) {
                    AlphabetGrid(
                        alphabet,
                        alphabetSelectedIndex,
                        onAlphabetIndexClicked,
                        Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = opened,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Box(
                Modifier
                    .padding(
                        bottom = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    )
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(secondaryColor)
                    .clickable(onClick = onCloseButtonClicked)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun AlphabetGrid(
    alphabet: List<String>,
    selectedIndex: Int,
    onAlphabetIndexClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) = FlowRow(
    mainAxisSize = SizeMode.Wrap,
    mainAxisSpacing = 16.dp,
    crossAxisSpacing = 16.dp,
    modifier = modifier
) {
    alphabet.forEachIndexed { index, letter ->
        val color = if (selectedIndex == index) {
            MaterialTheme.colors.background
        } else Color.Transparent

        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color)
                .clickable(
                    onClick = { onAlphabetIndexClicked(index) }
                )
        ) {
            Text(
                text = letter,
                color = MaterialTheme.colors.onBackground,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

private fun Modifier.clickableWithRippleEffect(
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val indication: Indication = rememberRipple(color = MaterialTheme.colors.primary)
    this.clickable(interactionSource, indication, enabled = enabled, onClick = onClick)
}

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
