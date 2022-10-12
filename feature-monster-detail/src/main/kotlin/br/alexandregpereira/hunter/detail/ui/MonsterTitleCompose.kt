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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.compose.HeaderFontSize
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.transition.HorizontalSlideTransition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MonsterTitleCompose(
    monsterTitleStates: List<MonsterTitleState>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    titleFontSize: MonsterTitleFontSize = MonsterTitleFontSize.LARGE,
    onOptionsClicked: () -> Unit = {}
) = Row(
    modifier
        .fillMaxWidth()
) {
    HorizontalSlideTransition(
        dataList = monsterTitleStates,
        pagerState,
        modifier = Modifier.weight(1f).clipToBounds()
    ) { data ->
        ScreenHeader(
            data.title,
            data.subTitle,
            Modifier
                .fillMaxWidth()
                .padding(
                    end = 8.dp
                ),
            titleFontSize = HeaderFontSize.valueOf(titleFontSize.name),
            contentPadding = contentPadding
        )
    }

    OptionIcon(
        Modifier
            .align(Alignment.CenterVertically)
            .padding(
                end = contentPadding.calculateEndPadding(LayoutDirection.Rtl),
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            ),
        onOptionsClicked
    )
}

@Composable
private fun OptionIcon(
    modifier: Modifier,
    onOptionsClicked: (() -> Unit)? = null
) {
    AppBarIcon(
        Icons.Filled.MoreVert,
        contentDescription = stringResource(R.string.monster_detail_options),
        modifier = modifier,
        onClicked = onOptionsClicked
    )
}

enum class MonsterTitleFontSize {
    LARGE, SMALL
}

data class MonsterTitleState(
    val title: String,
    val subTitle: String? = null
)

@Preview
@Composable
@OptIn(ExperimentalPagerApi::class)
private fun MonsterTitleWithSubtitlePreview() = Window {
    MonsterTitleCompose(
        listOf(
            MonsterTitleState(
                title = "Teste dos testes",
                subTitle = "Teste dos teste testado dos testes"
            )
        )
    ) {}
}

@Preview
@Composable
@OptIn(ExperimentalPagerApi::class)
private fun MonsterTitleComposePreview() = Window {
    MonsterTitleCompose(
        monsterTitleStates = listOf(
            MonsterTitleState(
                title = "Teste dos testes"
            )
        ),
        titleFontSize = MonsterTitleFontSize.SMALL,
        contentPadding = PaddingValues(
            top = 40.dp,
            bottom = 16.dp,
            start = 24.dp,
            end = 16.dp
        )
    ) {}
}
