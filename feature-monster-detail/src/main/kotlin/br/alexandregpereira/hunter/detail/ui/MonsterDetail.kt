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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.ui.compose.MonsterImage
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.theme.Shapes

@Composable
fun MonsterDetail(monster: Monster) {
    Column(
        Modifier
            .fillMaxHeight()
            .clip(Shapes.large)
            .background(Color(parseColor(monster.imageData.backgroundColor)))
    ) {
        MonsterImage(
            imageUrl = monster.imageData.url,
            contentDescription = monster.name,
            challengeRating = monster.challengeRating,
            type = MonsterItemType.valueOf(monster.type.name),
            fullOpen = true,
            isHorizontalImage = monster.imageData.isHorizontal
        )
    }
}