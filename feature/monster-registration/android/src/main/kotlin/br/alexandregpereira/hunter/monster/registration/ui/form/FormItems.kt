package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.monster.registration.ui.alsoAdd
import br.alexandregpereira.hunter.monster.registration.ui.alsoRemoveAt
import br.alexandregpereira.hunter.monster.registration.ui.strings

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
internal fun <T> LazyListScope.FormItems(
    items: MutableList<T>,
    addText: @Composable () -> String = { strings.add },
    removeText: @Composable () -> String = { strings.remove },
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
