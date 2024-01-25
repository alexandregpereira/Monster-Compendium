package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.ScreenHeader

@Suppress("FunctionName")
internal fun LazyListScope.FormLazy(
    key: String,
    title: @Composable () -> String,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    formItem(key = "$key-title") {
        Column(modifier) {
            Spacer(modifier = Modifier.height(16.dp))
            ScreenHeader(
                title = title(),
            )
        }
    }

    content()
}

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.formItem(
    key: String,
    modifier: Modifier = Modifier,
    content: @Composable LazyItemScope.() -> Unit
) = item(key) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .animateItemPlacement()
    ) {
        content()
    }
}
