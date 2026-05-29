package br.alexandregpereira.hunter.paywall

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
    val tryAgain: String
    val offerPeriodMonthly: String
    val offerPeriodYearly: String
    val offerPeriodWeekly: String
    val featureAccessToAllFeatures: String
    val featureNoAds: String
    val purchaseErrorTitle: String
    val purchaseErrorDescription: String
    val comeBackToOffer: String
    val restorePurchaseErrorTitle: String
    val restorePurchaseErrorDescription: String
    val subscriptionSuccessTitle: String
    val subscriptionSuccessDescription: String
    val buttonContinue: String
    val termsButton: String
    val privacyButton: String
    val termsUrl: String
    val privacyUrl: String
}

internal data class PaywallEnUsStrings(
    override val title: String = "Remove ads with Premium",
    override val description: String = "By subscribing to the premium plan, you remove ads and help maintain the project",
    override val subscribeButton: String = "Subscribe",
    override val restoreButton: String = "Restore",
    override val cancelAnytime: String = "Cancel anytime",
    override val featuresColumnHeader: String = "Features",
    override val freeColumnHeader: String = "Free",
    override val premiumColumnHeader: String = "Premium",
    override val offerFormattedPrice: String = "Only {value}/{period}",
    override val offerLoadErrorTitle: String = "Offer unavailable",
    override val offerLoadErrorDescription: String = "An error occurred while loading the offer.",
    override val tryAgain: String = "Try again",
    override val offerPeriodMonthly: String = "month",
    override val offerPeriodYearly: String = "year",
    override val offerPeriodWeekly: String = "week",
    override val featureAccessToAllFeatures: String = "Access to all features",
    override val featureNoAds: String = "No Ads",
    override val purchaseErrorTitle: String = "Subscription error",
    override val purchaseErrorDescription: String = "An error occurred during the subscription process. Please try again.",
    override val comeBackToOffer: String = "Come back to offer",
    override val restorePurchaseErrorTitle: String = "Do you have an active subscription?",
    override val restorePurchaseErrorDescription: String = "The restore subscription failed. Currently the subscription is only per device because we don't have a login yet (coming soon). Maybe you're trying to restore on a different device?",
    override val subscriptionSuccessTitle: String = "You are a Premium member now!",
    override val subscriptionSuccessDescription: String = "Thank you for subscribing! You can now enjoy all the features of the app with no ads.",
    override val buttonContinue: String = "Continue",
    override val termsButton: String = "Terms",
    override val privacyButton: String = "Privacy",
    override val termsUrl: String = "https://alexandregpereira.github.io/Monster-Compendium/docs/terms_of_use.html",
    override val privacyUrl: String = "https://alexandregpereira.github.io/Monster-Compendium/docs/privacy_policy.html",
) : PaywallStrings

internal data class PaywallPtBrStrings(
    override val title: String = "Remover anúncios com Premium",
    override val description: String = "Ao assinar o plano premium, você remove os anúncios e ajuda a manter o projeto",
    override val subscribeButton: String = "Assinar",
    override val restoreButton: String = "Restaurar",
    override val cancelAnytime: String = "Cancele quando quiser",
    override val featuresColumnHeader: String = "Recursos",
    override val freeColumnHeader: String = "Grátis",
    override val premiumColumnHeader: String = "Premium",
    override val offerFormattedPrice: String = "Apenas {value}/{period}",
    override val offerLoadErrorTitle: String = "Oferta indisponível",
    override val offerLoadErrorDescription: String = "Ocorreu um erro ao carregar a oferta.",
    override val tryAgain: String = "Tentar novamente",
    override val offerPeriodMonthly: String = "mês",
    override val offerPeriodYearly: String = "ano",
    override val offerPeriodWeekly: String = "semana",
    override val featureAccessToAllFeatures: String = "Acesso a todos os recursos",
    override val featureNoAds: String = "Sem anúncios",
    override val purchaseErrorTitle: String = "Erro na compra",
    override val purchaseErrorDescription: String = "Ocorreu um erro durante o processo de compra. Por favor, tente novamente.",
    override val comeBackToOffer: String = "Voltar para a oferta",
    override val restorePurchaseErrorTitle: String = "Você tem uma assinatura ativa?",
    override val restorePurchaseErrorDescription: String = "A restauração da assinatura falhou. Atualmente a assinatura é apenas por dispositivo porque ainda não temos um login (em breve). Talvez você esteja tentando restaurar em um dispositivo diferente?",
    override val subscriptionSuccessTitle: String = "Você é um membro Premium agora!",
    override val subscriptionSuccessDescription: String = "Obrigado por assinar! Agora você pode aproveitar todos os recursos do aplicativo sem anúncios.",
    override val buttonContinue: String = "Continuar",
    override val termsButton: String = "Termos",
    override val privacyButton: String = "Privacidade",
    override val termsUrl: String = "https://alexandregpereira.github.io/Monster-Compendium/docs/terms_of_use_pt_br.html",
    override val privacyUrl: String = "https://alexandregpereira.github.io/Monster-Compendium/docs/privacy_policy_pt_br.html",
) : PaywallStrings

internal fun PaywallStrings(): PaywallStrings = PaywallEnUsStrings()

internal data class PaywallEsStrings(
    override val title: String = "Eliminar anuncios con Premium",
    override val description: String = "Al suscribirte al plan premium, eliminas los anuncios y ayudas a mantener el proyecto",
    override val subscribeButton: String = "Suscribirse",
    override val restoreButton: String = "Restaurar",
    override val cancelAnytime: String = "Cancela cuando quieras",
    override val featuresColumnHeader: String = "Características",
    override val freeColumnHeader: String = "Gratis",
    override val premiumColumnHeader: String = "Premium",
    override val offerFormattedPrice: String = "Solo {value}/{period}",
    override val offerLoadErrorTitle: String = "Oferta no disponible",
    override val offerLoadErrorDescription: String = "Ocurrió un error al cargar la oferta.",
    override val tryAgain: String = "Intentar de nuevo",
    override val offerPeriodMonthly: String = "mes",
    override val offerPeriodYearly: String = "año",
    override val offerPeriodWeekly: String = "semana",
    override val featureAccessToAllFeatures: String = "Acceso a todas las funciones",
    override val featureNoAds: String = "Sin anuncios",
    override val purchaseErrorTitle: String = "Error en la compra",
    override val purchaseErrorDescription: String = "Ocurrió un error durante el proceso de compra. Por favor, intenta de nuevo.",
    override val comeBackToOffer: String = "Volver a la oferta",
    override val restorePurchaseErrorTitle: String = "¿Tienes una suscripción activa?",
    override val restorePurchaseErrorDescription: String = "La restauración de la suscripción falló. Actualmente la suscripción es solo por dispositivo porque aún no tenemos un inicio de sesión (próximamente). ¿Quizás estás intentando restaurar en un dispositivo diferente?",
    override val subscriptionSuccessTitle: String = "¡Ahora eres miembro Premium!",
    override val subscriptionSuccessDescription: String = "¡Gracias por suscribirte! Ahora puedes disfrutar de todas las funciones de la aplicación sin anuncios.",
    override val buttonContinue: String = "Continuar",
    override val termsButton: String = "Términos",
    override val privacyButton: String = "Privacidad",
    override val termsUrl: String = "https://alexandregpereira.github.io/Monster-Compendium/docs/terms_of_use_es.html",
    override val privacyUrl: String = "https://alexandregpereira.github.io/Monster-Compendium/docs/privacy_policy_es.html",
) : PaywallStrings

internal fun AppLocalization.getPaywallStrings(): PaywallStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> PaywallEnUsStrings()
        Language.PORTUGUESE -> PaywallPtBrStrings()
        Language.SPANISH -> PaywallEsStrings()
    }
}