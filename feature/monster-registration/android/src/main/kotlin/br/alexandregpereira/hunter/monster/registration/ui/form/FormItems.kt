package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.monster.registration.ui.alsoAdd
import br.alexandregpereira.hunter.monster.registration.ui.alsoRemoveAt

@Composable
internal fun <T> FormItems(
    items: MutableList<T>,
    addText: String,
    removeText: String,
    createNew: () -> T,
    onChanged: (List<T>) -> Unit = {},
    content: @Composable (Int, T) -> Unit
) {
    AddRemoveButtons(
        addText = addText,
        removeText = removeText.takeUnless { items.isEmpty() }.orEmpty(),
        onAdd = {
            onChanged(items.alsoAdd(0, createNew()))
        },
        onRemove = {
            onChanged(items.alsoRemoveAt(0))
        }
    )

    items.forEachIndexed { index, item ->
        content(index, item)

        AddRemoveButtons(
            addText = addText,
            removeText = removeText.takeUnless { index == items.lastIndex }.orEmpty(),
            onAdd = {
                onChanged(items.alsoAdd(index + 1, createNew()))
            },
            onRemove = {
                onChanged(items.alsoRemoveAt(index + 1))
            }
        )
    }
}
