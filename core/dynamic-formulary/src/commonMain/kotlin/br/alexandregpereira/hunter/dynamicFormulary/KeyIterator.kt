package br.alexandregpereira.hunter.dynamicFormulary

class KeyIterator(
    private val keysList: List<String> = emptyList(),
) : Iterator<String> {
    private var keys: Iterator<String> = keysList.iterator()

    override fun hasNext(): Boolean = keys.hasNext()

    override fun next(): String {
        if (!hasNext()) {
            keys = keysList.iterator()
        }

        return keys.next()
    }
}