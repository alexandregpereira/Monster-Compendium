package br.alexandregpereira.hunter.ads

internal data class AdsState(
    val isVisible: Boolean = false,
    val isAdSlotReady: Boolean = false,
    val isAdConsentGranted: Boolean = false,
    val strings: AdsStrings = AdsStrings(),
) {

    /**
     * The ad banner is only rendered when the user consented to ads. Without consent there is no
     * ad to request, so the slot stays with the promo banner instead of cycling through failed
     * requests.
     */
    val isAdBannerVisible: Boolean
        get() = isVisible && isAdSlotReady && isAdConsentGranted

    val isPromoBannerVisible: Boolean
        get() = isVisible && isAdBannerVisible.not()
}
