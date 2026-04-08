package br.alexandregpereira.hunter.data.strings

import br.alexandregpereira.hunter.database.StringEntityQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class StringsLocalDataSource(
    private val queries: StringEntityQueries,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun getStrings(lang: String): Map<String, String> = withContext(dispatcher) {
        queries.getStrings(lang).executeAsList().associate { it.key to it.value_ }
    }

    suspend fun saveStrings(lang: String, strings: Map<String, String>) = withContext(dispatcher) {
        queries.transaction {
            queries.deleteStrings(lang)
            strings.forEach { (key, value) ->
                queries.insertString(key, lang, value)
            }
        }
    }
}
