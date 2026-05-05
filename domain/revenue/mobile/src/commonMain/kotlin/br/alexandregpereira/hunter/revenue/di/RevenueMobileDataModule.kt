/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.revenue.di

import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.GetCurrentOfferImpl
import br.alexandregpereira.hunter.revenue.IsPremium
import br.alexandregpereira.hunter.revenue.IsPremiumImpl
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReachedImpl
import br.alexandregpereira.hunter.revenue.Purchase
import br.alexandregpereira.hunter.revenue.PurchaseImpl
import br.alexandregpereira.hunter.revenue.RestorePurchase
import br.alexandregpereira.hunter.revenue.RestorePurchaseImpl
import br.alexandregpereira.hunter.revenue.RevenueMobileSdk
import br.alexandregpereira.hunter.revenue.RevenueSdk
import br.alexandregpereira.hunter.revenue.RevenueSession
import br.alexandregpereira.hunter.revenue.RevenueSessionImpl
import br.alexandregpereira.hunter.revenue.RevenueSessionRemoteConfig
import br.alexandregpereira.hunter.revenue.RevenueSessionTimeDataSource
import com.russhwolf.settings.Settings
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val revenueMobileDataModule get() = module {
    val preferenceName = "monster-compendium-feature-usage"
    single<Settings>(qualifier = qualifier(preferenceName)) {
        get<Settings.Factory>().create(preferenceName)
    }
    factory<IsSessionUsageLimitReached> {
        IsSessionUsageLimitReachedImpl(
            isPremium = get(),
            revenueSessionRemoteConfig = get(),
            revenueSessionTimeDataSource = get(),
            analytics = get(),
            featureFlagProvider = get(),
        )
    }
    factory<RevenueSdk> {
        RevenueMobileSdk()
    }
    factory {
        RevenueSessionTimeDataSource(
            settings = get<Settings>(qualifier(preferenceName)),
        )
    }
    factory {
        RevenueSessionRemoteConfig(
            client = get(),
            json = get(),
        )
    }
    factory<IsPremium> {
        IsPremiumImpl(
            revenueSdk = get(),
            settings = get<Settings>(qualifier(preferenceName)),
            analytics = get(),
        )
    }
    single<RevenueSession> {
        RevenueSessionImpl(
            revenueSessionTimeDataSource = get(),
            analytics = get(),
            isPremium = get(),
            revenueSdk = get(),
        )
    }
    factory<Purchase> {
        PurchaseImpl(
            revenueSdk = get(),
        )
    }
    factory<RestorePurchase> {
        RestorePurchaseImpl(
            revenueSdk = get(),
            isPremium = get(),
        )
    }
    factory<GetCurrentOffer> {
        GetCurrentOfferImpl(
            revenueSdk = get(),
        )
    }
}
