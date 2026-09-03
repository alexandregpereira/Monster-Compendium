package br.alexandregpereira.hunter.ads

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

internal interface AdsStrings {
    val promoBannerTitle: String
    val promoBannerMessage: String
    val promoBannerButton: String
}

internal data class AdsEnUsStrings(
    override val promoBannerTitle: String = "Subscribe to Premium",
    override val promoBannerMessage: String = "Claim this screen space and remove the ads",
    override val promoBannerButton: String = "Subscribe",
) : AdsStrings

internal data class AdsPtBrStrings(
    override val promoBannerTitle: String = "Assine o plano Premium",
    override val promoBannerMessage: String = "Reivindique esse espaço da tela e remova os anúncios",
    override val promoBannerButton: String = "Assinar",
) : AdsStrings

internal data class AdsEsStrings(
    override val promoBannerTitle: String = "Suscríbete al plan Premium",
    override val promoBannerMessage: String = "Reclama este espacio de la pantalla y elimina los anuncios",
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
