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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.compose.ChallengeRatingCircle
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.compose.MonsterTypeIcon
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterDetail(
    monsters: List<Monster>,
    initialMonsterIndex: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val pagerState = rememberPagerState(
        pageCount = monsters.size,
        initialPage = initialMonsterIndex
    )

    val monster = monsters[pagerState.currentPage]
    val pageOffset = pagerState.currentPage + pagerState.currentPageOffset
    val nextMonsterIndex = when {
        pageOffset < pagerState.currentPage -> pagerState.currentPage - 1
        pageOffset > pagerState.currentPage -> pagerState.currentPage + 1
        else -> pagerState.currentPage
    }
    val nextMonster = monsters[nextMonsterIndex]
    val type: MonsterItemType = MonsterItemType.valueOf(monster.type.name)
    val nextType: MonsterItemType = MonsterItemType.valueOf(nextMonster.type.name)

    val fraction = pagerState.currentPageOffset
    val alpha = lerp(
        start = 1f,
        stop = 0f,
        fraction = fraction
    )

    val nextAlpha = lerp(
        start = 0f,
        stop = 1f,
        fraction = fraction
    )

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val startColor = monster.imageData.backgroundColor.getColor(isSystemInDarkTheme)
    val endColor = nextMonster.imageData.backgroundColor.getColor(isSystemInDarkTheme)

    val backgroundColor = lerp(
        start = startColor.toColor(),
        stop = endColor.toColor(),
        fraction = fraction
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
    )

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState(),
            )
    ) {
        Box(
            Modifier
                .height(420.dp)
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            MonsterImages(
                images = monsters.map { Image(it.imageData.url, it.name) },
                pagerState = pagerState,
                height = 420.dp,
                shape = RectangleShape
            )

            ChallengeRatingCircle(
                challengeRating = monster.challengeRating,
                size = 56.dp,
                fontSize = 16.sp,
                modifier = Modifier.alpha(alpha)
            )

            ChallengeRatingCircle(
                challengeRating = nextMonster.challengeRating,
                size = 56.dp,
                fontSize = 16.sp,
                modifier = Modifier.alpha(nextAlpha)
            )

            MonsterTypeIcon(type = type, iconSize = 32.dp, Modifier.alpha(alpha))
            MonsterTypeIcon(type = nextType, iconSize = 32.dp, Modifier.alpha(nextAlpha))
        }
        AnimatedVisibility(
            visible = true,
            initiallyVisible = false,
            enter = slideIn(
                initialOffset = { IntOffset(x = 0, y = it.height) },
                animationSpec = spring(stiffness = 100f, dampingRatio = 0.65f)
            )
        ) {
            MonsterInfo(
                monster,
                contentPadding = contentPadding,
                modifier = Modifier.alpha(alpha)
            )
            MonsterInfo(
                nextMonster,
                contentPadding = contentPadding,
                modifier = Modifier.alpha(nextAlpha)
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun MonsterDetailPreview() = Window {
    MonsterDetail(
        monsters = (0..10).map {
            Monster(
                index = "",
                type = MonsterType.CELESTIAL,
                subtype = null,
                group = null,
                challengeRating = 0.0f,
                name = "",
                subtitle = "",
                imageData = MonsterImageData(
                    url = "",
                    backgroundColor = br.alexandregpereira.hunter.domain.model.Color(
                        light = "#ffe2e2",
                        dark = "#ffe2e2"
                    ),
                    isHorizontal = false
                ),
                size = "",
                alignment = "",
                stats = Stats(
                    armorClass = 0,
                    hitPoints = 0,
                    hitDice = ""
                ),
                speed = Speed(hover = false, values = listOf()),
                abilityScores = listOf(),
                savingThrows = listOf(),
                skills = listOf(),
                damageVulnerabilities = listOf(),
                damageResistances = listOf(),
                damageImmunities = listOf()
            )
        },
        initialMonsterIndex = 2
    )
}