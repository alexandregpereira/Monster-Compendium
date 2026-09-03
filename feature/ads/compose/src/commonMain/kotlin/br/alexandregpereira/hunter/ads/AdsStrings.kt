package br.alexandregpereira.hunter.ads

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

internal interface AdsStrings {
    val promoBannerMessage: String
    val promoBannerButton: String
}

internal data class AdsEnUsStrings(
    override val promoBannerMessage: String = "Claim this screen space by subscribing to Premium and removing ads",
    override val promoBannerButton: String = "Subscribe",
) : AdsStrings

internal data class AdsPtBrStrings(
    override val promoBannerMessage: String = "Reivindique esse espaço da tela assinando o Premium e removendo os anúncios",
    override val promoBannerButton: String = "Assinar",
) : AdsStrings

internal data class AdsEsStrings(
    override val promoBannerMessage: String = "Reclama este espacio de la pantalla suscribiéndote a Premium y eliminando los anuncios",
    override val promoBannerButton: String = "Suscribirse",
) : AdsStrings

internal fun AdsStrings(): AdsStrings = AdsEnUsStrings()

internal fun AppLocalization.getAdsStrings(): AdsStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> AdsEnUsStrings()
        Language.PORTUGUESE -> AdsPtBrStrings()
        Language.SPANISH -> AdsEsStrings()
    }
}
