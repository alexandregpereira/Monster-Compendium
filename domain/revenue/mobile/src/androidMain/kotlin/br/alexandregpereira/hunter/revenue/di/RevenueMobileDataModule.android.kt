package br.alexandregpereira.hunter.revenue.di

import br.alexandregpereira.hunter.revenue.RevenueMobileSdk
import br.alexandregpereira.hunter.revenue.RevenueSdk

internal actual fun createRevenueSdk(): RevenueSdk {
    return RevenueMobileSdk()
}
