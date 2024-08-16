package br.alexandregpereira.hunter.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize

@Composable
internal fun SearchKeyButtons(
    searchKeys: List<SearchKeyState>,
    shouldShow: () -> Boolean,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) = AnimatedVisibility(
    visible = shouldShow(),
    enter = fadeIn(),
    exit = fadeOut(),
) {
    Row(
        modifier = modifier.horizontalScroll(
            state = rememberScrollState(),
        ).padding(horizontal = 16.dp),
    ) {
        searchKeys.forEachIndexed { index, searchKey ->
            SearchKeyButton(searchKey = searchKey, onClick = { onClick(index) })
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
internal fun SearchKeyButton(
    searchKey: SearchKeyState,
    onClick: () -> Unit,
) = AppButton(
    text = searchKey.keyWithSymbols,
    size = AppButtonSize.VERY_SMALL,
    onClick = onClick,
)
