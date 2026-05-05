package br.alexandregpereira.hunter.revenue

interface RevenueSession {

    fun initialize(apiKey: String)
    fun start()
    fun stop()
}
