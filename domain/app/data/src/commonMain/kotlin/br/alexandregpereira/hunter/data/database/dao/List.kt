package br.alexandregpereira.hunter.data.database.dao

internal fun <T> List<List<T>>.reduceList(): List<T> {
    return this.reduceOrNull { acc, list -> acc + list } ?: emptyList()
}

internal fun <T, R> List<T>.mapAndReduce(transform: T.() -> List<R>): List<R> {
    return this.map(transform).reduceList()
}
