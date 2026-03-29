package br.alexandregpereira.hunter.revenue

data class Offer(
    val id: String,
    val value: String,
    val period: OfferPeriod,
)

enum class OfferPeriod {
    WEEKLY,
    MONTHLY,
    YEARLY,
}
