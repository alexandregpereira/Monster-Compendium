package br.alexandregpereira.hunter.ui.registration

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
fun <T> LazyListScope.FormItems(
    items: MutableList<T>,
    addText: @Composable () -> String,
    removeText: @Composable () -> String,
    keys: Iterator<String>,
    createNew: () -> T,
    onChanged: (List<T>) -> Unit = {},
    content: LazyListScope.(Int, T) -> Unit
) {
    formItem(key = keys.next()) {
        AddRemoveButtons(
            addText = addText(),
            removeText = removeText().takeUnless { items.isEmpty() }.orEmpty(),
            onAdd = {
                onChanged(items.alsoAdd(0, createNew()))
            },
            onRemove = {
                onChanged(items.alsoRemoveAt(0))
            },
        )
    }

    items.forEachIndexed { index, item ->
        content(index, item)

        formItem(key = keys.next()) {
            AddRemoveButtons(
                addText = addText(),
                removeText = removeText().takeUnless { index == items.lastIndex }.orEmpty(),
                onAdd = {
                    onChanged(items.alsoAdd(index + 1, createNew()))
                },
                onRemove = {
                    onChanged(items.alsoRemoveAt(index + 1))
                },
                modifier = Modifier.animateItemPlacement(),
            )
        }
    }
}

fun <T> MutableList<T>.alsoAdd(index: Int, value: T): List<T> {
    return also { it.add(index, value) }
}

fun <T> MutableList<T>.alsoRemoveAt(index: Int): List<T> {
    return also { it.removeAt(index) }
}
