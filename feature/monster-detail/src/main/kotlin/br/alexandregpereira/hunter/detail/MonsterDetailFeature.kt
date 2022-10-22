/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)

package br.alexandregpereira.hunter.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.MonsterDetailScreen
import br.alexandregpereira.hunter.ui.compose.CircularLoading
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun MonsterDetailFeature(
    contentPadding: PaddingValues = PaddingValues(0.dp),
) = HunterTheme {
    val viewModel: MonsterDetailViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    BackHandler(enabled = viewState.showDetail) {
        viewModel.onClose()
    }

    AnimatedVisibility(
        visible = viewState.showDetail,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = slideOutVertically { fullHeight -> fullHeight },
    ) {
        CircularLoading(
            isLoading = viewState.isLoading,
            modifier = Modifier.background(MaterialTheme.colors.surface)
        ) {
            if (viewState.monsters.isEmpty()) return@CircularLoading
            MonsterDetailScreen(
                viewState.monsters,
                viewState.initialMonsterIndex,
                contentPadding,
                onMonsterChanged = { monster ->
                    viewModel.onMonsterChanged(monster.index)
                },
                onOptionsClicked = viewModel::onShowOptionsClicked,
                onSpellClicked = viewModel::onSpellClicked
            )

            MonsterDetailOptionPicker(
                options = viewState.options,
                showOptions = viewState.showOptions,
                onOptionSelected = viewModel::onOptionClicked,
                onClosed = viewModel::onShowOptionsClosed
            )
        }
    }
}
