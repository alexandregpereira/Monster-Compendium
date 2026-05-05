package br.alexandregpereira.hunter.condition

fun interface GetCondition {
    suspend operator fun invoke(index: String): Condition
}
