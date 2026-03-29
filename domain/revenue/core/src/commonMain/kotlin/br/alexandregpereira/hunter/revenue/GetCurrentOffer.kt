package br.alexandregpereira.hunter.revenue

fun interface GetCurrentOffer {
    @Throws(GetCurrentOfferException::class)
    suspend operator fun invoke(): Offer
}

sealed class GetCurrentOfferException(message: String) : Throwable(message) {
    class OfferNotFound(internalMessage: String) : GetCurrentOfferException(
        message = "Offer not found: $internalMessage",
    )
}
