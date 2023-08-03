package br.alexandregpereira.hunter.analytics.di

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.analytics.FirebaseAnalytics
import org.koin.core.scope.Scope

internal actual fun Scope.createAnalytics(): Analytics = FirebaseAnalytics(
    analytics = get(),
    crashlytics = get()
)
