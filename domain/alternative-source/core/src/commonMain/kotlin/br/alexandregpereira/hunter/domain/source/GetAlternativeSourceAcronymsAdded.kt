package br.alexandregpereira.hunter.domain.source

fun interface GetAlternativeSourceAcronymsAdded {
    suspend operator fun invoke(): List<String>
}
