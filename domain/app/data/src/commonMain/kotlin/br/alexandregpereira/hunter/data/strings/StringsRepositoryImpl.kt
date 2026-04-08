package br.alexandregpereira.hunter.data.strings

import br.alexandregpereira.hunter.domain.strings.StringsRepository
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class StringsRepositoryImpl(
    private val remote: StringsRemoteDataSource,
    private val local: StringsLocalDataSource,
) : StringsRepository {

    private val mutex = Mutex()
    private var cachedLang: String? = null
    private var cachedStrings: Map<String, String> = emptyMap()

    override suspend fun getStrings(lang: String): Map<String, String> {
        mutex.withLock {
            if (cachedLang == lang) return cachedStrings
        }
        // DB read happens outside the lock — suspending inside a lock would
        // block other coroutines for the full duration of the IO call.
        return local.getStrings(lang).also { strings ->
            mutex.withLock {
                cachedLang = lang
                cachedStrings = strings
            }
        }
    }

    override suspend fun syncStrings(lang: String) {
        val strings = remote.getStrings(lang).single()
        local.saveStrings(lang, strings)
        mutex.withLock {
            if (cachedLang == lang) {
                cachedStrings = strings
            }
        }
    }
}
