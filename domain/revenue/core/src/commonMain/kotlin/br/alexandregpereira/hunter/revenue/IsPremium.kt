package br.alexandregpereira.hunter.revenue

fun interface IsPremium {
    suspend operator fun invoke(): Boolean
}
