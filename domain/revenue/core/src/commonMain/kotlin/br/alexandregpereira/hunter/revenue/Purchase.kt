package br.alexandregpereira.hunter.revenue

fun interface Purchase {
    suspend operator fun invoke(offerId: String)
}
