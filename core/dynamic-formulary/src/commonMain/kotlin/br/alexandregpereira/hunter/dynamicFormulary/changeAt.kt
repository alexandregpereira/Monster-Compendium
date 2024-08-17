package br.alexandregpereira.hunter.dynamicFormulary

fun <T> MutableList<T>.changeAt(
    index: Int,
    copy: T.() -> T
): List<T> {
    return also {
        it[index] = it[index].copy()
    }
}
