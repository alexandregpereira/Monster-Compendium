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

package br.alexandregpereira.hunter.ui.compose.tablecontent

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.BackHandler
import br.alexandregpereira.hunter.ui.compose.noIndicationClick
import kotlin.math.ln

@Composable
fun TableContentPopup(
    tableContent: List<TableContentItemState>,
    tableContentSelectedIndex: Int,
    opened: Boolean,
    onOpenButtonClicked: () -> Unit,
    onCloseButtonClicked: () -> Unit,
    onTableContentClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    alphabet: List<String> = emptyList(),
    alphabetSelectedIndex: Int = 0,
    tableContentOpened: Boolean = false,
    tableContentInitialIndex: Int = 0,
    icon: ImageVector? = null,
    backHandlerEnabled: Boolean = tableContentOpened,
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

    BackHandler(enabled = backHandlerEnabled, onBack = onTableContentClosed)

    Box(modifier, contentAlignment = Alignment.BottomEnd) {
        val backgroundColor = if (MaterialTheme.colors.isLight) MaterialTheme.colors.surface else {
            val alpha = ((4.5f * ln(elevation.toFloat() + 1)) + 2f) / 100f
            MaterialTheme.colors.onSurface.copy(alpha = alpha)
                .compositeOver(MaterialTheme.colors.surface)
        }
        Box(
            modifier = Modifier
                .padding(
                    vertical = shadowingVerticalPadding,
                    horizontal = shadowingHorizontalPadding
                )
                .sizeIn(minWidth = 56.dp, minHeight = 56.dp)
                .clip(shape = RoundedCornerShape(percent))
                .background(color = backgroundColor)
                .noIndicationClick()
                .animateContentSize(
                    animationSpec = spring(Spring.DampingRatioLowBouncy)
                )
        ) {

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
        ) {
            CloseButton(
                backgroundColor = MaterialTheme.colors.background,
                onClick = onCloseButtonClicked
            )
        }

        AnimatedVisibility(
            visible = opened.not(),
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
            exit = fadeOut(),
        ) {
            if (icon != null) {
                CircleIcon(
                    icon = icon,
                    onClick = onOpenButtonClicked
                )
            } else {
                CircleLetter(
                    letter = alphabet.getOrNull(alphabetSelectedIndex) ?: "",
                    onClick = onOpenButtonClicked
                )
            }
        }
    }
}

private val shadowingHorizontalPadding = 2.dp
private val shadowingVerticalPadding = 8.dp
private const val elevation = 4

@Composable
private fun CircleLetter(
    letter: String,
    onClick: () -> Unit
) = Circle(
    onClick = onClick,
) {
    Text(
        text = letter,
        style = TextStyle(fontSize = 24.sp),
        modifier = Modifier
            .align(Alignment.Center)
    )
}

@Composable
private fun Circle(
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) = Surface(
    elevation = elevation.dp,
    shape = CircleShape,
    modifier = Modifier.padding(
        vertical = shadowingVerticalPadding,
        horizontal = shadowingHorizontalPadding
    )
) {
    Box(
        Modifier
            .size(56.dp)
            .clickableWithRippleEffect(onClick = onClick)
    ) {
        content()
    }
}

@Composable
private fun CircleIcon(
    icon: ImageVector,
    onClick: () -> Unit
) = Circle(
    onClick = onClick,
) {
    Icon(
        imageVector = icon,
        contentDescription = "Open",
        modifier = Modifier
            .align(Alignment.Center),
    )
}

@Composable
private fun AlphabetGrid(
    alphabet: List<String>,
    selectedIndex: Int,
    onAlphabetIndexClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) = FlowRow(
    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
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

@Composable
private fun CloseButton(backgroundColor: Color, onClick: () -> Unit) {
    Box(
        Modifier
            .padding(
                bottom = 16.dp,
                end = 16.dp,
                top = 8.dp
            )
            .padding(vertical = shadowingVerticalPadding, horizontal = shadowingHorizontalPadding)
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.align(Alignment.Center)
        )
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
