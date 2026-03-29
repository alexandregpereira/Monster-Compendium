package br.alexandregpereira.hunter.revenue

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

internal interface PaywallStrings {
    val title: String
    val description: String
    val subscribeButton: String
    val restoreButton: String
    val cancelAnytime: String
    val featuresColumnHeader: String
    val freeColumnHeader: String
    val premiumColumnHeader: String
    val offerFormattedPrice: String
    val offerLoadErrorTitle: String
    val offerLoadErrorDescription: String
    val offerLoadErrorTryAgain: String
    val offerPeriodMonthly: String
    val offerPeriodYearly: String
    val offerPeriodWeekly: String
    val featureAccessToAllFeatures: String
    val featureNoAds: String
    val featureFutureExclusiveContent: String
}

internal data class PaywallEnUsStrings(
    override val title: String = "Subscribe to support the project",
    override val description: String = "With your support we will continue to maintaining the project",
    override val subscribeButton: String = "Subscribe",
    override val restoreButton: String = "Restore subscription",
    override val cancelAnytime: String = "Cancel anytime",
    override val featuresColumnHeader: String = "Features",
    override val freeColumnHeader: String = "Free",
    override val premiumColumnHeader: String = "Premium",
    override val offerFormattedPrice: String = "Only {value}/{period}",
    override val offerLoadErrorTitle: String = "Offer unavailable",
    override val offerLoadErrorDescription: String = "An error occurred while loading the offer.",
    override val offerLoadErrorTryAgain: String = "Try again",
    override val offerPeriodMonthly: String = "month",
    override val offerPeriodYearly: String = "year",
    override val offerPeriodWeekly: String = "week",
    override val featureAccessToAllFeatures: String = "Access to all features",
    override val featureNoAds: String = "No Ads",
    override val featureFutureExclusiveContent: String = "Future exclusive content",
) : PaywallStrings

internal data class PaywallPtBrStrings(
    override val title: String = "Assine para apoiar o projeto",
    override val description: String = "Com o seu apoio continuaremos mantendo o projeto",
    override val subscribeButton: String = "Assinar",
    override val restoreButton: String = "Restaurar assinatura",
    override val cancelAnytime: String = "Cancele quando quiser",
    override val featuresColumnHeader: String = "Recursos",
    override val freeColumnHeader: String = "Grátis",
    override val premiumColumnHeader: String = "Premium",
    override val offerFormattedPrice: String = "Apenas {value}/{period}",
    override val offerLoadErrorTitle: String = "Oferta indisponível",
    override val offerLoadErrorDescription: String = "Ocorreu um erro ao carregar a oferta.",
    override val offerLoadErrorTryAgain: String = "Tentar novamente",
    override val offerPeriodMonthly: String = "mês",
    override val offerPeriodYearly: String = "ano",
    override val offerPeriodWeekly: String = "semana",
    override val featureAccessToAllFeatures: String = "Acesso a todos os recursos",
    override val featureNoAds: String = "Sem anúncios",
    override val featureFutureExclusiveContent: String = "Futuro conteúdo exclusivo",
) : PaywallStrings

internal fun PaywallStrings(): PaywallStrings = PaywallEnUsStrings()

internal fun AppLocalization.getPaywallStrings(): PaywallStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> PaywallEnUsStrings()
        Language.PORTUGUESE -> PaywallPtBrStrings()
    }
}