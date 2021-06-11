/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.MonsterDetailOption
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.compose.noIndicationClick

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MonsterDetailOptionPicker(
    options: List<MonsterDetailOption>,
    showOptions: Boolean,
    onOptionSelected: (MonsterDetailOption) -> Unit = {},
    onClosed: () -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showOptions,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .noIndicationClick(onClick = onClosed)
            )
        }

        AnimatedVisibility(
            visible = showOptions,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .noIndicationClick()
        ) {
            MonsterDetailOptions(options, onOptionSelected)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MonsterDetailOptions(
    options: List<MonsterDetailOption>,
    onOptionSelected: (MonsterDetailOption) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = animatePressed(pressed = isPressed)

    Surface(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Column {
            Text(
                text = stringResource(R.string.monster_detail_options),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(all = 16.dp)
            )

            options.forEach {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onOptionSelected(it) }
                        )
                ) {
                    Text(
                        text = it.name,
                        fontSize = 16.sp,
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(PaddingValues(all = 32.dp).calculateBottomPadding())
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MonsterDetailOptionPickerPreview() {
    MonsterDetailOptionPicker(
        listOf(
            MonsterDetailOption.CHANGE_TO_FEET,
            MonsterDetailOption.CHANGE_TO_METERS
        ),
        showOptions = true
    )
}