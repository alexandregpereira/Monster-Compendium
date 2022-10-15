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
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.compose.ChallengeRatingCircle
import br.alexandregpereira.hunter.ui.compose.MonsterTypeIcon
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.transition.AlphaTransition
import br.alexandregpereira.hunter.ui.transition.getTransitionData
import br.alexandregpereira.hunter.ui.util.toColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterDetailScreen(
    monsters: List<MonsterState>,
    initialMonsterIndex: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pagerState: PagerState = rememberPagerState(
        initialPage = initialMonsterIndex
    ),
    scrollState: ScrollState = rememberScrollState(),
    onMonsterChanged: (monster: MonsterState) -> Unit = {},
    onOptionsClicked: () -> Unit = {},
    onSpellClicked: (String) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
            )
            .animateContentSize()
    ) {
        MonsterImageCompose(monsters, pagerState, contentPadding)

        Box(
            Modifier
                .fillMaxSize()
                .monsterImageBackground(monsters, pagerState)
        ) {
            val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            MonsterTitleCompose(
                monsterTitleStates = monsters.map {
                    MonsterTitleState(
                        title = it.name,
                        subTitle = it.subtitle
                    )
                },
                pagerState = pagerState,
                onOptionsClicked = onOptionsClicked,
                modifier = Modifier
                    .clip(shape)
                    .background(
                        shape = shape,
                        color = MaterialTheme.colors.surface
                    )
            )
        }

        MonsterInfo(
            monsters = monsters,
            pagerState = pagerState,
            scrollState = scrollState,
            contentPadding = contentPadding,
            onSpellClicked = onSpellClicked
        )
    }

    MonsterTopBar(
        monsters,
        pagerState,
        scrollState,
        contentPadding = PaddingValues(
            top = 16.dp + contentPadding.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        onOptionsClicked = onOptionsClicked
    )
    OnMonsterChanged(monsters, pagerState, onMonsterChanged)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun OnMonsterChanged(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    onMonsterChanged: (monster: MonsterState) -> Unit
) {
    val transitionData = getTransitionData(dataList = monsters, pagerState)
    onMonsterChanged(transitionData.data)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MonsterImageCompose(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Box(
        Modifier
            .monsterImageBackground(monsters, pagerState)
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        MonsterImages(
            images = monsters.map { ImageState(it.imageState.url, it.name) },
            pagerState = pagerState,
            height = IMAGE_HEIGHT,
            shape = RectangleShape,
            contentPadding = PaddingValues(
                top = MONSTER_IMAGE_COMPOSE_TOP_PADDING,
                bottom = MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING
            )
        )

        ChallengeRatingCompose(monsters, pagerState)

        MonsterTypeIcon(
            monsters = monsters,
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun Modifier.monsterImageBackground(
    monsters: List<MonsterState>,
    pagerState: PagerState,
) = composed {
    val transitionData = getTransitionData(monsters, pagerState)

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val startColor = transitionData.data.imageState.backgroundColor.getColor(isSystemInDarkTheme)
    val endColor = transitionData.nextData.imageState.backgroundColor.getColor(isSystemInDarkTheme)

    val backgroundColor = lerp(
        start = startColor.toColor(),
        stop = endColor.toColor(),
        fraction = transitionData.fraction
    )

    this.background(backgroundColor)
}

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MonsterTopBar(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    scrollState: ScrollState,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onOptionsClicked: () -> Unit,
) {
    val imageHeightInPixels = LocalDensity.current.run { IMAGE_HEIGHT.toPx() }
    val contentPaddingTotalInPixels = LocalDensity.current.run {
        (MONSTER_IMAGE_COMPOSE_TOP_PADDING + MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING +
                contentPadding.calculateTopPadding()).toPx()
    }

    AnimatedVisibility(
        visible = scrollState.value >= (imageHeightInPixels + contentPaddingTotalInPixels),
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {
        Column {
            Row(
                Modifier
                    .background(color = MaterialTheme.colors.surface),
            ) {

                val composableScope = rememberCoroutineScope()
                AppBarIcon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.monster_detail_go_to_top),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(
                            start = contentPadding.calculateStartPadding(LayoutDirection.Rtl),
                            top = contentPadding.calculateTopPadding(),
                            bottom = contentPadding.calculateBottomPadding(),
                        ),
                    onClicked = {
                        composableScope.launch {
                            scrollState.animateScrollTo(value = 0)
                        }
                    }
                )

                MonsterTitleCompose(
                    monsterTitleStates = monsters.map { MonsterTitleState(title = it.name) },
                    pagerState = pagerState,
                    titleFontSize = MonsterTitleFontSize.SMALL,
                    contentPadding = PaddingValues(
                        top = contentPadding.calculateTopPadding(),
                        bottom = contentPadding.calculateBottomPadding(),
                        start = 24.dp,
                        end = 16.dp
                    ),
                    onOptionsClicked = onOptionsClicked,
                )
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ChallengeRatingCompose(
    monsters: List<MonsterState>,
    pagerState: PagerState,
) {
    AlphaTransition(dataList = monsters, pagerState) { data: MonsterState, alpha: Float ->
        ChallengeRatingCircle(
            challengeRating = data.imageState.challengeRating,
            size = 56.dp,
            fontSize = 16.sp,
            modifier = Modifier.alpha(alpha)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MonsterTypeIcon(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    AlphaTransition(dataList = monsters, pagerState) { data: MonsterState, alpha ->
        MonsterTypeIcon(
            iconRes = data.imageState.type.iconRes,
            iconSize = 32.dp,
            modifier.alpha(alpha)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
private fun MonsterInfo(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    scrollState: ScrollState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onSpellClicked: (String) -> Unit = {}
) {
    val imageHeightInPixels = LocalDensity.current.run { IMAGE_HEIGHT.toPx() }
    val contentPaddingTotalInPixels = LocalDensity.current.run {
        (MONSTER_IMAGE_COMPOSE_TOP_PADDING + MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING +
                contentPadding.calculateTopPadding() + 200.dp).toPx()
    }
    val enableGesture = scrollState.value < (imageHeightInPixels + contentPaddingTotalInPixels)

    MonsterInfo(
        monsters = monsters,
        pagerState = pagerState,
        contentPadding = contentPadding,
        enableGesture = enableGesture,
        onSpellClicked = onSpellClicked
    )
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
private fun MonsterInfo(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    enableGesture: Boolean = true,
    onSpellClicked: (String) -> Unit = {}
) = AlphaTransition(
    dataList = monsters,
    pagerState = pagerState,
    enableGesture = enableGesture
) { data: MonsterState, alpha: Float ->
    MonsterInfo(
        data,
        contentPadding = contentPadding,
        alpha = alpha,
        onSpellClicked = onSpellClicked
    )
}

private val MONSTER_IMAGE_COMPOSE_TOP_PADDING = 24.dp
private val MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING = 16.dp
private val IMAGE_HEIGHT = 420.dp

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Preview
@Composable
private fun MonsterDetailPreview() = Window {
    MonsterDetailScreen(
        monsters = (0..10).map {
            MonsterState(
                index = "",
                name = "Monster of the monsters",
                imageState = MonsterImageState(
                    url = "",
                    type = MonsterTypeState.CELESTIAL,
                    challengeRating = 0.0f,
                    backgroundColor = ColorState(
                        light = "#ffe2e2",
                        dark = "#ffe2e2"
                    ),
                    isHorizontal = false
                ),
                subtype = null,
                group = null,
                subtitle = "This is the subtitle",
                size = "Large",
                alignment = "Good",
                stats = StatsState(
                    armorClass = 0,
                    hitPoints = 0,
                    hitDice = ""
                ),
                speed = SpeedState(hover = false, values = listOf()),
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
                reactions = listOf()
            )
        },
        initialMonsterIndex = 2
    )
}

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun MonsterTopBarPreview() = Window {
    MonsterTopBar(
        monsters = listOf(
            MonsterState(
                index = "",
                name = "Monster of the monsters",
                imageState = MonsterImageState(
                    url = "",
                    type = MonsterTypeState.CELESTIAL,
                    challengeRating = 0.0f,
                    backgroundColor = ColorState(
                        light = "#ffe2e2",
                        dark = "#ffe2e2"
                    ),
                    isHorizontal = false
                ),
                subtype = null,
                group = null,
                subtitle = "This is the subtitle",
                size = "Large",
                alignment = "Good",
                stats = StatsState(
                    armorClass = 0,
                    hitPoints = 0,
                    hitDice = ""
                ),
                speed = SpeedState(hover = false, values = listOf()),
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
                reactions = listOf()
            )
        ),
        pagerState = rememberPagerState(),
        scrollState = rememberScrollState(Int.MAX_VALUE)
    ) {

    }
}
