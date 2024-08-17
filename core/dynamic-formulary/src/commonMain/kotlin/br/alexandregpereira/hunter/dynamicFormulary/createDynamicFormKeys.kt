package br.alexandregpereira.hunter.dynamicFormulary

fun <T> List<T>.createDynamicFormKeys(
    key: Any,
    getItemKey: (T) -> String,
    hasTitle: Boolean = true,
    addKeys: MutableList<String>.(T) -> Unit,
): List<String> {
    val list = this
    return buildList {
        if (hasTitle) add("$key")
        add("$key-add-remove-buttons")
        list.forEach { item ->
            mutableListOf<String>().also {
                it.addKeys(item)
            }.forEach {
                add("$key-$it-${getItemKey(item)}")
            }
            add("$key-add-remove-buttons-${getItemKey(item)}")
        }
    }
}
