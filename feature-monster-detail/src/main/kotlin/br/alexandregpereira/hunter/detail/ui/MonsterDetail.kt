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
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.compose.ChallengeRatingCircle
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.compose.MonsterTypeIcon
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterDetail(
    monsters: List<Monster>,
    initialMonsterIndex: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMonsterChanged: (monster: Monster) -> Unit = {},
    onOptionsClicked: () -> Unit = {}
) {
    val pagerState = rememberPagerState(
        pageCount = monsters.size,
        initialPage = initialMonsterIndex,
        initialOffscreenLimit = 2
    )

    BackGroundColor(monsters = monsters, pagerState = pagerState)

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

            MonsterImageInfo(
                monsters = monsters,
                pagerState = pagerState,
            )
        }

        MonsterInfo(
            monsters = monsters,
            pagerState = pagerState,
            contentPadding = contentPadding,
            onMonsterChanged = onMonsterChanged,
            onOptionsClicked = onOptionsClicked
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BackGroundColor(
    monsters: List<Monster>,
    pagerState: PagerState
) {
    val transitionData = getTransitionData(monsters, pagerState)

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val startColor = transitionData.monster.imageData.backgroundColor.getColor(isSystemInDarkTheme)
    val endColor =
        transitionData.nextMonster.imageData.backgroundColor.getColor(isSystemInDarkTheme)
    val fraction = pagerState.currentPageOffset.absoluteValue.coerceIn(0f, 1f)

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
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterImageInfo(
    monsters: List<Monster>,
    pagerState: PagerState
) {
    val transitionData = getTransitionData(monsters, pagerState)

    val type: MonsterItemType = MonsterItemType.valueOf(transitionData.monster.type.name)
    val nextType: MonsterItemType = MonsterItemType.valueOf(transitionData.nextMonster.type.name)

    ChallengeRatingCircle(
        challengeRating = transitionData.monster.challengeRating,
        size = 56.dp,
        fontSize = 16.sp,
        modifier = Modifier.alpha(transitionData.alpha)
    )

    ChallengeRatingCircle(
        challengeRating = transitionData.nextMonster.challengeRating,
        size = 56.dp,
        fontSize = 16.sp,
        modifier = Modifier.alpha(transitionData.nextAlpha)
    )

    MonsterTypeIcon(type = type, iconSize = 32.dp, Modifier.alpha(transitionData.alpha))
    MonsterTypeIcon(type = nextType, iconSize = 32.dp, Modifier.alpha(transitionData.nextAlpha))
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
fun MonsterInfo(
    monsters: List<Monster>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMonsterChanged: (monster: Monster) -> Unit,
    onOptionsClicked: () -> Unit
) {
    val transitionData = getTransitionData(monsters, pagerState)
    onMonsterChanged(transitionData.monster)

    AnimatedVisibility(
        visible = true,
        enter = slideIn(
            initialOffset = { IntOffset(x = 0, y = it.height) },
            animationSpec = spring(stiffness = 100f, dampingRatio = 0.65f)
        )
    ) {
        MonsterInfo(
            transitionData.monster,
            contentPadding = contentPadding,
            alpha = transitionData.alpha,
            onOptionsClicked = onOptionsClicked
        )
        if (transitionData.monster != transitionData.nextMonster) {
            MonsterInfo(
                transitionData.nextMonster,
                contentPadding = contentPadding,
                modifier = Modifier.alpha(transitionData.nextAlpha),
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun getTransitionData(
    monsters: List<Monster>,
    pagerState: PagerState
): TransitionData {
    val monster = monsters[pagerState.currentPage]
    val pageOffset = pagerState.currentPage + pagerState.currentPageOffset
    val nextMonsterIndex = when {
        pageOffset < pagerState.currentPage -> pagerState.currentPage - 1
        pageOffset > pagerState.currentPage -> pagerState.currentPage + 1
        else -> pagerState.currentPage
    }
    val nextMonster = monsters[nextMonsterIndex]

    val fraction = pagerState.currentPageOffset.absoluteValue.coerceIn(0f, 1f)

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

    return TransitionData(monster, nextMonster, alpha, nextAlpha)
}

data class TransitionData(
    val monster: Monster,
    val nextMonster: Monster,
    val alpha: Float,
    val nextAlpha: Float
)

@ExperimentalAnimationApi
@Preview
@Composable
fun MonsterDetailPreview() = Window {
    MonsterDetail(
        monsters = (0..10).map {
            Monster(
                preview = MonsterPreview(
                    index = "",
                    type = MonsterType.CELESTIAL,
                    challengeRating = 0.0f,
                    name = "",
                    imageData = MonsterImageData(
                        url = "",
                        backgroundColor = Color(
                            light = "#ffe2e2",
                            dark = "#ffe2e2"
                        ),
                        isHorizontal = false
                    ),
                ),
                subtype = null,
                group = null,
                subtitle = "",
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
                damageImmunities = listOf(),
                conditionImmunities = listOf(),
                senses = listOf(),
                languages = "Test",
                specialAbilities = listOf(),
                actions = listOf(),
            )
        },
        initialMonsterIndex = 2
    )
}