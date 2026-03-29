package br.alexandregpereira.hunter.revenue

interface Purchase {
    suspend operator fun invoke(offerId: String)
}
