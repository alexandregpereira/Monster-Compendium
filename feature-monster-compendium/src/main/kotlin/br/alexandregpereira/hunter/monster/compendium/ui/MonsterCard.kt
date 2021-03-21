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

package br.alexandregpereira.hunter.monster.compendium.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.ui.MonsterImage
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun MonsterCard(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    challengeRating: Float,
) {
    MonsterImage(
        imageUrl = imageUrl,
        backgroundColor = backgroundColor,
        contentDescription = contentDescription,
        challengeRating = challengeRating,
    )
}

@Preview
@Composable
fun MonsterCardPreview() {
    HunterTheme {
        MonsterCard(
            "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/aboleth.png",
            "#80e3efef",
            "any",
            22f,
        )
    }
}
