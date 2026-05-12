package br.alexandregpereira.hunter.paywall

internal sealed class PaywallViewAction {
    data class GoToUrl(val url: String) : PaywallViewAction()
}
