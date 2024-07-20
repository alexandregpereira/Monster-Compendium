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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.MonsterType.CELESTIAL
import br.alexandregpereira.hunter.monster.detail.ColorState
import br.alexandregpereira.hunter.monster.detail.MonsterImageState
import br.alexandregpereira.hunter.monster.detail.MonsterState
import br.alexandregpereira.hunter.monster.detail.SpeedState
import br.alexandregpereira.hunter.monster.detail.StatsState
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.compose.BoxCloseButton
import br.alexandregpereira.hunter.ui.compose.ChallengeRatingCircle
import br.alexandregpereira.hunter.ui.compose.LocalScreenSize
import br.alexandregpereira.hunter.ui.compose.MonsterTypeIcon
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.compose.getTintColor
import br.alexandregpereira.hunter.ui.transition.AlphaTransition
import br.alexandregpereira.hunter.ui.transition.getPageOffset
import br.alexandregpereira.hunter.ui.transition.getTransitionData
import br.alexandregpereira.hunter.ui.transition.transitionHorizontalScrollable
import br.alexandregpereira.hunter.ui.util.toColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonsterDetailScreen(
    monsters: List<MonsterState>,
    initialMonsterIndex: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pagerState: PagerState = rememberPagerState(
        initialPage = initialMonsterIndex,
        pageCount = { monsters.size }
    ),
    scrollState: LazyListState = rememberLazyListState(),
    onMonsterChanged: (monster: MonsterState) -> Unit = {},
    onOptionsClicked: () -> Unit = {},
    onSpellClicked: (String) -> Unit = {},
    onLoreClicked: (String) -> Unit = {},
    onClose: () -> Unit = {},
) = Surface {
    HorizontalPagerTransitionController(pagerState)

    MonsterImageCompose(
        monsters,
        pagerState,
        contentPadding = contentPadding,
    )

    /*
    To avoid seeing the image below LazyColumn when horizontal pager transition is happening,
    a background was added at the bottom of the Monster title and below LazyColumn.
    When the scrolls changes and the Monster title moves, the background offset is updated to
    follow the scroll.
    */
    ScrollableBackground(
        getScrollPositionOffset = {
            scrollState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == MONSTER_TITLE_ITEM_KEY }
                ?.let { itemInfo: LazyListItemInfo ->
                    itemInfo.offset.coerceAtLeast(0)
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
                        getImageHeightInDp()
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
            val shape = remember { RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) }
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

    BoxCloseButton(onClick = onClose)

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
    OnMonsterChanged(monsters, initialMonsterIndex, pagerState, onMonsterChanged)
}

@Composable
private fun HorizontalPagerTransitionController(pagerState: PagerState) = HorizontalPager(
    state = pagerState,
) {
    Box(Modifier.fillMaxWidth())
}

@Composable
private fun ScrollableBackground(
    getScrollPositionOffset: () -> Int
) = Layout(
    content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    shape = remember { RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) },
                    color = MaterialTheme.colors.surface
                )
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
    initialMonsterIndex: Int,
    pagerState: PagerState,
    onMonsterChanged: (monster: MonsterState) -> Unit
) {
    var initialMonsterIndexState by remember { mutableIntStateOf(initialMonsterIndex) }

    LaunchedEffect(key1 = initialMonsterIndex) {
        pagerState.scrollToPage(initialMonsterIndex)
    }

    LaunchedEffect(pagerState, monsters) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (initialMonsterIndexState == initialMonsterIndex) {
                onMonsterChanged(monsters[page])
            } else {
                initialMonsterIndexState = initialMonsterIndex
            }
        }
    }
}

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
            images = monsters.map { ImageState(it.imageUrl, it.name) },
            pagerState = pagerState,
            height = getImageHeightInDp(),
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
    val startColor = transitionData.data.getBackgroundColor(isSystemInDarkTheme)
    val endColor = transitionData.nextData.getBackgroundColor(isSystemInDarkTheme)

    val backgroundColor = lerp(
        start = startColor.toColor(),
        stop = endColor.toColor(),
        fraction = transitionData.fraction
    )

    this.background(backgroundColor)
}

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
                    contentDescription = strings.goToTop,
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

@Composable
private fun ChallengeRatingCompose(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    contentTopPadding: Dp = 0.dp
) {
    AlphaTransition(dataList = monsters, pagerState, modifier = modifier) { data: MonsterState ->
        ChallengeRatingCircle(
            challengeRating = data.challengeRating,
            xp = data.xp,
            size = 62.dp,
            fontSize = 18.sp,
            xpFontSize = 12.sp,
            contentTopPadding = contentTopPadding
        )
    }
}

@Composable
private fun MonsterTypeIcon(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    AlphaTransition(dataList = monsters, pagerState, modifier = modifier) { data: MonsterState ->
        MonsterTypeIcon(
            icon = data.type.toIcon(),
            iconSize = 32.dp,
            tint = data.getBackgroundColor(isSystemInDarkTheme()).getTintColor()
        )
    }
}

@Composable
private fun getImageHeightInDp(): Dp {
    val screenSizeInfo = LocalScreenSize.current
    return (screenSizeInfo.hDP.value * 0.75).dp
}

private val MONSTER_IMAGE_COMPOSE_TOP_PADDING = 24.dp
private val MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING = 16.dp
private const val MONSTER_TITLE_ITEM_KEY = "MonsterTitleCompose"

@OptIn(ExperimentalAnimationApi::class)
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
                    type = CELESTIAL,
                    challengeRating = "1",
                    xp = "100 XP",
                    backgroundColor = ColorState(
                        light = "#ffe2e2",
                        dark = "#ffe2e2"
                    ),
                ),
                subtitle = "This is the subtitle",
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
                    type = CELESTIAL,
                    challengeRating = "1",
                    xp = "100 XP",
                    backgroundColor = ColorState(
                        light = "#ffe2e2",
                        dark = "#ffe2e2"
                    ),
                ),
                subtitle = "This is the subtitle",
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
        pagerState = rememberPagerState(
            pageCount = { 1 }
        ),
        firstVisibleItemIndex = { 0 },
        onOptionsClicked = {

        },
        scrollToTop = {}
    )
}
