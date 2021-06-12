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
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.compose.ChallengeRatingCircle
import br.alexandregpereira.hunter.ui.compose.MonsterItemType
import br.alexandregpereira.hunter.ui.compose.MonsterTypeIcon
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterDetail(
    monsters: List<Monster>,
    initialMonsterIndex: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pagerState: PagerState = rememberPagerState(
        pageCount = monsters.size,
        initialPage = initialMonsterIndex,
        initialOffscreenLimit = 2
    ),
    scrollState: ScrollState = rememberScrollState(),
    onMonsterChanged: (monster: Monster) -> Unit = {},
    onOptionsClicked: () -> Unit = {}
) {
    BackGroundColor(monsters = monsters, pagerState = pagerState)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState,
            )
    ) {
        MonsterImageCompose(monsters, pagerState, contentPadding)

        val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        MonsterTitleCompose(
            monsterTitleStates = monsters.map { MonsterTitleState(title = it.name, subTitle = it.subtitle) },
            pagerState = pagerState,
            onOptionsClicked = onOptionsClicked,
            modifier = Modifier
                .clip(shape)
                .background(
                    shape = shape,
                    color = MaterialTheme.colors.surface
                )
        )

        MonsterInfo(
            monsters = monsters,
            pagerState = pagerState,
            contentPadding = contentPadding,
            onMonsterChanged = onMonsterChanged,
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
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MonsterImageCompose(
    monsters: List<Monster>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Box(
        Modifier
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        MonsterImages(
            images = monsters.map { Image(it.imageData.url, it.name) },
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
@Composable
private fun BackGroundColor(
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


@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MonsterTopBar(
    monsters: List<Monster>,
    pagerState: PagerState,
    scrollState: ScrollState,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onOptionsClicked: () -> Unit
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
    monsters: List<Monster>,
    pagerState: PagerState
) {
    val transitionData = getTransitionData(monsters, pagerState)

    ChallengeRatingCircle(
        challengeRating = transitionData.monster.challengeRating,
        size = 56.dp,
        fontSize = 16.sp,
        modifier = Modifier.alpha(transitionData.alpha)
    )

    if (transitionData.monster != transitionData.nextMonster) {
        ChallengeRatingCircle(
            challengeRating = transitionData.nextMonster.challengeRating,
            size = 56.dp,
            fontSize = 16.sp,
            modifier = Modifier.alpha(transitionData.nextAlpha)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MonsterTypeIcon(
    monsters: List<Monster>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val transitionData = getTransitionData(monsters, pagerState)

    val type: MonsterItemType = MonsterItemType.valueOf(transitionData.monster.type.name)
    val nextType: MonsterItemType = MonsterItemType.valueOf(transitionData.nextMonster.type.name)

    MonsterTypeIcon(type = type, iconSize = 32.dp, modifier.alpha(transitionData.alpha))

    if (transitionData.monster != transitionData.nextMonster) {
        MonsterTypeIcon(type = nextType, iconSize = 32.dp, modifier.alpha(transitionData.nextAlpha))
    }
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalAnimationApi
@Composable
private fun MonsterInfo(
    monsters: List<Monster>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMonsterChanged: (monster: Monster) -> Unit,
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
        Box(
            modifier = Modifier.scrollable(
                orientation = Orientation.Horizontal,
                flingBehavior = PagerDefaults.defaultPagerFlingConfig(pagerState),
                state = pagerState
            )
        ) {
            MonsterInfo(
                transitionData.monster,
                contentPadding = contentPadding,
                alpha = transitionData.alpha,
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

private data class TransitionData(
    val monster: Monster,
    val nextMonster: Monster,
    val alpha: Float,
    val nextAlpha: Float
)

private val MONSTER_IMAGE_COMPOSE_TOP_PADDING = 24.dp
private val MONSTER_IMAGE_COMPOSE_BOTTOM_PADDING = 16.dp
private val IMAGE_HEIGHT = 420.dp

@ExperimentalAnimationApi
@Preview
@Composable
private fun MonsterDetailPreview() = Window {
    MonsterDetail(
        monsters = (0..10).map {
            Monster(
                preview = MonsterPreview(
                    index = "",
                    type = MonsterType.CELESTIAL,
                    challengeRating = 0.0f,
                    name = "Monster of the monsters",
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
                subtitle = "This is the subtitle",
                size = "Large",
                alignment = "Good",
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

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun MonsterTopBarPreview() = Window {
    MonsterTopBar(
        monsters = listOf(
            Monster(
                preview = MonsterPreview(
                    index = "",
                    type = MonsterType.CELESTIAL,
                    challengeRating = 0.0f,
                    name = "Monster of the monsters",
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
                subtitle = "This is the subtitle",
                size = "Large",
                alignment = "Good",
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
        ),
        pagerState = rememberPagerState(pageCount = 1),
        scrollState = rememberScrollState(Int.MAX_VALUE)
    ) {

    }
}