/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail.ui

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.ui.compose.MonsterImage
import br.alexandregpereira.hunter.ui.compose.MonsterItemType

@ExperimentalAnimationApi
@Composable
fun MonsterDetail(
    monster: Monster,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Box(
        Modifier
            .fillMaxHeight()
            .background(
                Color(
                    parseColor(
                        monster.imageData.backgroundColor.getColor(isSystemInDarkTheme())
                    )
                )
            )
    ) {
        MonsterImage(
            imageUrl = monster.imageData.url,
            contentDescription = monster.name,
            challengeRating = monster.challengeRating,
            type = MonsterItemType.valueOf(monster.type.name),
            fullOpen = true,
            modifier = Modifier.padding(contentPadding)
        )

        AnimatedVisibility(
            visible = true,
            initiallyVisible = false,
            enter = slideIn(
                initialOffset = { IntOffset(x = 0, y = it.height) },
                animationSpec = spring(stiffness = 100f, dampingRatio = 0.65f)
            )
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical
                    )
            ) {
                Spacer(modifier = Modifier.height(420.dp))
                MonsterInfo(monster)
            }
        }
    }
}