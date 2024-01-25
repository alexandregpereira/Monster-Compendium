package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.alsoAdd
import br.alexandregpereira.hunter.monster.registration.ui.alsoRemoveAt

@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
internal fun <T> LazyListScope.FormItems(
    items: MutableList<T>,
    addText: @Composable () -> String = { stringResource(R.string.monster_registration_add) },
    removeText: @Composable () -> String = { stringResource(R.string.monster_registration_remove) },
    key: String,
    createNew: () -> T,
    onChanged: (List<T>) -> Unit = {},
    content: LazyListScope.(Int, T) -> Unit
) {
    formItem(key = "$key-add-remove-buttons") {
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

        formItem(key = "$key-add-remove-buttons-$index") {
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
