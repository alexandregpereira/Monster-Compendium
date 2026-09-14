/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.spell.compendium.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.compendium.EmptySpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumIntent
import br.alexandregpereira.hunter.spell.compendium.SpellCompendiumState
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.AppFullScreen
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.AppTopBar
import br.alexandregpereira.hunter.ui.compose.AppTopBarIconButton
import br.alexandregpereira.hunter.ui.compose.BackHandler

@Composable
internal fun SpellCompendiumScreen(
    state: SpellCompendiumState,
    contentPadding: PaddingValues,
    intent: SpellCompendiumIntent = EmptySpellCompendiumIntent(),
) = AppFullScreen(
    isOpen = state.isShowing,
    contentPaddingValues = contentPadding,
    showCloseButton = false,
    onClose = intent::onClose
) {
    BackHandler(enabled = state.isSearchOpened, onBack = intent::onSearchClose)

    val density = LocalDensity.current
    val listState = rememberLazyGridState(initialFirstVisibleItemIndex = state.initialItemIndex)
    var topBarHeight by remember { mutableStateOf(0.dp) }
    Box(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            SpellList(
                spellsGroupByLevel = state.spellsGroupByLevel,
                initialItemIndex = state.initialItemIndex,
                listState = listState,
                contentPadding = PaddingValues(
                    top = topBarHeight,
                    bottom = 16.dp + contentPadding.calculateBottomPadding(),
                ),
                intent = intent
            )
        }

        AnimatedContent(
            targetState = state.isSearchOpened,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "SpellCompendiumTopBar",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    topBarHeight = with(density) { size.height.toDp() }
                },
        ) { isSearchOpened ->
            val topBarModifier = Modifier.padding(top = contentPadding.calculateTopPadding())
            if (isSearchOpened) {
                AppTopBar(
                    listState = listState,
                    navigationIcon = {
                        AppTopBarIconButton(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            onClick = intent::onSearchClose,
                        )
                    },
                    modifier = topBarModifier,
                    titleContent = {
                        SpellCompendiumSearchField(
                            text = state.searchText,
                            label = state.searchTextLabel,
                            onValueChange = intent::onSearchTextChange,
                        )
                    },
                )
            } else {
                AppTopBar(
                    title = state.title,
                    listState = listState,
                    onCloseClick = intent::onClose,
                    modifier = topBarModifier,
                    actions = {
                        AppTopBarIconButton(
                            imageVector = Icons.Filled.Search,
                            contentDescription = state.searchTextLabel,
                            onClick = intent::onSearchClick,
                        )
                    },
                )
            }
        }

        AppCircleButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = 16.dp + contentPadding.calculateBottomPadding(),
                ),
            onClick = intent::onAddSpell,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun SpellCompendiumSearchField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    AppTextField(
        text = text,
        label = label,
        capitalize = false,
        modifier = Modifier.focusRequester(focusRequester),
        onValueChange = onValueChange,
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
