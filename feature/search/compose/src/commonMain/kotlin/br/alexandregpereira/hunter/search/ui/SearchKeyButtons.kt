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
    initialScrollOffset: Int,
    shouldShow: () -> Boolean,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    onScrollChanges: (Int) -> Unit,
) {
    val scrollState = rememberScrollState(initial = initialScrollOffset)
    onScrollChanges(
        getScrollPosition = { scrollState.value },
        onScrollChanges = onScrollChanges,
    )
    AnimatedVisibility(
        visible = shouldShow(),
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            modifier = modifier.horizontalScroll(
                state = scrollState,
            ).padding(horizontal = 16.dp),
        ) {
            searchKeys.forEachIndexed { index, searchKey ->
                SearchKeyButton(searchKey = searchKey, onClick = { onClick(index) })
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun onScrollChanges(
    getScrollPosition: () -> Int,
    onScrollChanges: (Int) -> Unit,
) {
    onScrollChanges(getScrollPosition())
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
