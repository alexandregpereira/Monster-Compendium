package br.alexandregpereira.hunter.domain.strings

interface StringsRepository {
    suspend fun getStrings(lang: String): Map<String, String>
    suspend fun syncStrings(lang: String)
}
