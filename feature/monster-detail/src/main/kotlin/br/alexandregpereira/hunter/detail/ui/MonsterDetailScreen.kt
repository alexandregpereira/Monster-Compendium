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

@file:OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.compose.ChallengeRatingCircle
import br.alexandregpereira.hunter.ui.compose.MonsterTypeIcon
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.transition.AlphaTransition
import br.alexandregpereira.hunter.ui.transition.getPageOffset
import br.alexandregpereira.hunter.ui.transition.getTransitionData
import br.alexandregpereira.hunter.ui.transition.transitionHorizontalScrollable
import br.alexandregpereira.hunter.ui.util.toColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
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
    scrollState: LazyListState = rememberLazyListState(),
    onMonsterChanged: (monster: MonsterState) -> Unit = {},
    onOptionsClicked: () -> Unit = {},
    onSpellClicked: (String) -> Unit = {},
    onLoreClicked: (String) -> Unit = {}
) = Surface {

    MonsterImageCompose(
        monsters,
        pagerState,
        contentPadding = contentPadding,
    )

    ScrollableBackground(
        getScrollPositionOffset = {
            scrollState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == MONSTER_TITLE_ITEM_KEY }
                ?.run {
                    offset.coerceAtLeast(0) + (size / 2)
                } ?: 0
        }
    )

    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "MonsterImageCompose") {
            Box(
                modifier = Modifier
                    .height(
                        IMAGE_HEIGHT
                                + MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING
                                + MONSTER_IMAGE_COMPOSE_TOP_PADDING
                                + contentPadding.calculateTopPadding()
                    )
                    .fillMaxWidth()
                    .transitionHorizontalScrollable(pagerState)
                    .animateItemPlacement()
            )
        }

        item(key = MONSTER_TITLE_ITEM_KEY) {
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
                    .animateItemPlacement()
            )
        }

        monsterInfo(
            monsters = monsters,
            pagerState = pagerState,
            contentPadding = contentPadding,
            getItemsKeys = { scrollState.layoutInfo.visibleItemsInfo.map { it.key } },
            onSpellClicked = onSpellClicked,
            onLoreClick = onLoreClicked
        )
    }

    MonsterTopBar(
        monsters,
        pagerState,
        firstVisibleItemIndex = { scrollState.firstVisibleItemIndex },
        contentPadding = PaddingValues(
            top = 16.dp + contentPadding.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        onOptionsClicked = onOptionsClicked,
        scrollToTop = {
            scrollState.animateScrollToItem(0)
        }
    )
    OnMonsterChanged(monsters, getPageOffset = { pagerState.getPageOffset() }, onMonsterChanged)
}

@Composable
private fun ScrollableBackground(
    getScrollPositionOffset: () -> Int
) = Layout(
    content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
        )
    }
) { measurables, constraints ->
    val placeable = measurables.first().measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) {
        val offset = getScrollPositionOffset()
        placeable.placeRelative(x = 0, y = offset)
    }
}

@Composable
private fun OnMonsterChanged(
    monsters: List<MonsterState>,
    getPageOffset: () -> Float,
    onMonsterChanged: (monster: MonsterState) -> Unit
) {
    val transitionData = getTransitionData(dataList = monsters, getPageOffset = getPageOffset)
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
            .fillMaxSize()
            .monsterImageBackground(monsters, getPageOffset = { pagerState.getPageOffset() })
    ) {
        MonsterImages(
            images = monsters.map { ImageState(it.imageState.url, it.name) },
            pagerState = pagerState,
            height = IMAGE_HEIGHT,
            shape = RectangleShape,
            contentPadding = PaddingValues(
                top = MONSTER_IMAGE_COMPOSE_TOP_PADDING + contentPadding.calculateTopPadding(),
                bottom = MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING
            )
        )

        ChallengeRatingCompose(
            monsters,
            pagerState,
            contentTopPadding = contentPadding.calculateTopPadding()
        )

        MonsterTypeIcon(
            monsters = monsters,
            pagerState = pagerState,
            modifier = Modifier.padding(top = contentPadding.calculateTopPadding())
        )
    }
}

private fun Modifier.monsterImageBackground(
    monsters: List<MonsterState>,
    getPageOffset: () -> Float,
) = composed {
    val transitionData = getTransitionData(monsters, getPageOffset)

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
    contentPadding: PaddingValues = PaddingValues(16.dp),
    firstVisibleItemIndex: () -> Int,
    scrollToTop: suspend CoroutineScope.() -> Unit,
    onOptionsClicked: () -> Unit,
) {
    val visible by remember {
        derivedStateOf {
            firstVisibleItemIndex() > 0
        }
    }

    AnimatedVisibility(
        visible = visible,
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
                        composableScope.launch(block = scrollToTop)
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
    modifier: Modifier = Modifier,
    contentTopPadding: Dp = 0.dp
) {
    AlphaTransition(dataList = monsters, pagerState, modifier = modifier) { data: MonsterState ->
        ChallengeRatingCircle(
            challengeRating = data.imageState.challengeRating,
            size = 56.dp,
            fontSize = 16.sp,
            contentTopPadding = contentTopPadding
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
    AlphaTransition(dataList = monsters, pagerState, modifier = modifier) { data: MonsterState ->
        MonsterTypeIcon(
            iconRes = data.imageState.type.iconRes,
            iconSize = 32.dp,
        )
    }
}

private val MONSTER_IMAGE_COMPOSE_TOP_PADDING = 24.dp
private val MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING = 16.dp
private val IMAGE_HEIGHT = 420.dp
private const val MONSTER_TITLE_ITEM_KEY = "MonsterTitleCompose"

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
        firstVisibleItemIndex = { 0 },
        onOptionsClicked = {

        },
        scrollToTop = {}
    )
}
