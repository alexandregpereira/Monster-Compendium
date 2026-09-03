package br.alexandregpereira.hunter.ads

internal data class AdsState(
    val isVisible: Boolean = false,
    val isAdSlotReady: Boolean = false,
    val strings: AdsStrings = AdsStrings(),
) {

    val isPromoBannerVisible: Boolean
        get() = isVisible && isAdSlotReady.not()

    val isAdBannerVisible: Boolean
        get() = isVisible && isAdSlotReady
}
