package br.alexandregpereira.hunter.condition

fun interface SyncConditions {
    suspend operator fun invoke()
}
