package br.alexandregpereira.hunter.domain.source

import br.alexandregpereira.hunter.domain.source.model.AlternativeSource

fun interface GetAlternativeSourcesAdded {
    suspend operator fun invoke(): List<AlternativeSource>
}
