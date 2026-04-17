package br.alexandregpereira.hunter.condition

fun interface GetConditions {
    suspend operator fun invoke(): List<Condition>
}
