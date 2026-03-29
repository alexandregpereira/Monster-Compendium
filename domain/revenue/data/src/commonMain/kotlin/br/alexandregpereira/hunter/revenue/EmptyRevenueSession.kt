package br.alexandregpereira.hunter.revenue

internal class EmptyRevenueSession : RevenueSession {

    override fun initialize(apiKey: String) {}

    override fun start() {}

    override fun stop() {}
}
