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

package br.alexandregpereira.hunter.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.BottomNavigation
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.app.BottomBarItem
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.util.BottomNavigationHeight
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
internal fun BoxScope.AppBottomNavigation(
    showBottomBar: Boolean,
    bottomBarItemSelectedIndex: Int,
    bottomBarItems: List<BottomBarItem>,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (BottomBarItem) -> Unit = {}
) = HunterTheme {
    AnimatedVisibility(
        visible = showBottomBar,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
        modifier = Modifier.align(Alignment.BottomStart)
    ) {
        Window(elevation = 4.dp) {
            val paddingBottom = contentPadding.calculateBottomPadding()
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                modifier = Modifier.height(BottomNavigationHeight + paddingBottom)
                    .padding(bottom = paddingBottom)
            ) {
                bottomBarItems.forEachIndexed { i, bottomBarItem ->
                    AppBottomNavigationItem(
                        totalItems = bottomBarItems.size,
                        indexSelected = bottomBarItemSelectedIndex,
                        currentIndex = i,
                        icon = bottomBarItem.icon.value,
                        name = bottomBarItem.text,
                        onClick = { onClick(bottomBarItem) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.AppBottomNavigationItem(
    totalItems: Int,
    indexSelected: Int,
    currentIndex: Int,
    icon: DrawableResource,
    name: String,
    onClick: () -> Unit
) {
    AppBottomNavigationItem(
        selected = indexSelected == currentIndex,
        totalItems = totalItems,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = name
            )
        },
        label = {
            Text(text = name, maxLines = 1)
        },
    )
}

@Composable
private fun RowScope.AppBottomNavigationItem(
    selected: Boolean,
    totalItems: Int,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val styledLabel: @Composable (() -> Unit) = label.let {
        @Composable {
            val style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(style, content = label)
        }
    }
    val ripple = rememberRipple(bounded = false, color = selectedContentColor)

    BottomNavigationTransition(
        selectedContentColor,
        unselectedContentColor,
        selected
    ) { animationProgress ->
        Box(
            modifier
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = ripple
                )
                .weight(
                    lerp(
                        start = 1f / totalItems,
                        stop = (1f / totalItems) * 1.5f,
                        fraction = animationProgress
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            BottomNavigationItemBaselineLayout(
                icon = icon,
                label = styledLabel,
                iconPositionAnimationProgress = animationProgress,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun BottomNavigationTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable (animationProgress: Float) -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f, label = "BottomNavigationTransition",
    )

    val color = lerp(inactiveColor, activeColor, animationProgress)

    CompositionLocalProvider(
        LocalContentColor provides color.copy(alpha = 1f),
        LocalContentAlpha provides color.alpha,
    ) {
        content(animationProgress)
    }
}

@Composable
private fun BottomNavigationItemBaselineLayout(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    iconPositionAnimationProgress: Float,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            Box(Modifier.layoutId("icon")) { icon() }
            Box(
                Modifier
                    .layoutId("label")
                    .alpha(iconPositionAnimationProgress)
                    .padding(horizontal = 16.dp)
            ) { label() }
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)

        val labelPlaceable = label.let {
            measurables.first { it.layoutId == "label" }.measure(Constraints())
        }

        placeLabelAndIcon(
            labelPlaceable,
            iconPlaceable,
            constraints,
            iconPositionAnimationProgress
        )
    }
}

private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    constraints: Constraints,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    iconPositionAnimationProgress: Float
): MeasureResult {
    val baseline = labelPlaceable[LastBaseline]
    val labelBaselineOffset = 12.dp.roundToPx()
    // Label should be [ItemLabelBaselineBottomOffset] from the bottom
    val labelY = constraints.maxHeight - baseline - labelBaselineOffset
    val labelX =
        (constraints.maxWidth - labelPlaceable.width) / 2

    // Icon should be [ItemIconTopOffset] from the top when selected
    val selectedIconY = 8.dp.roundToPx()
    val unselectedIconY = (constraints.maxHeight - iconPlaceable.height) / 2
    val iconX = (constraints.maxWidth - iconPlaceable.width) / 2
    // How far the icon needs to move between unselected and selected states
    val iconDistance = unselectedIconY - selectedIconY

    // When selected the icon is above the unselected position, so we will animate moving
    // downwards from the selected state, so when progress is 1, the total distance is 0, and we
    // are at the selected state.
    val offset = (iconDistance * (1 - iconPositionAnimationProgress)).roundToInt()

    return layout(constraints.maxWidth, constraints.maxHeight) {
        if (iconPositionAnimationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, selectedIconY + offset)
    }
}
