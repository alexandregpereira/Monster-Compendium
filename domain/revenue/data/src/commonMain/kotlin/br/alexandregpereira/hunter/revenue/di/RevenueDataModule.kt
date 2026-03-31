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

import br.alexandregpereira.hunter.revenue.EmptyRevenueSession
import br.alexandregpereira.hunter.revenue.GetCurrentOffer
import br.alexandregpereira.hunter.revenue.IsPremium
import br.alexandregpereira.hunter.revenue.IsSessionUsageLimitReached
import br.alexandregpereira.hunter.revenue.Purchase
import br.alexandregpereira.hunter.revenue.RestorePurchase
import br.alexandregpereira.hunter.revenue.RevenueSession
import org.koin.dsl.module

val revenueDataModule get() = module {
    factory<IsSessionUsageLimitReached> {
        IsSessionUsageLimitReached { false }
    }
    factory<IsPremium> {
        IsPremium { true }
    }
    single<RevenueSession> {
        EmptyRevenueSession()
    }
    factory<Purchase> {
        Purchase { throw NotImplementedError("Purchase is not implemented yet") }
    }
    factory<RestorePurchase> {
        RestorePurchase { throw NotImplementedError("RestorePurchase is not implemented yet") }
    }
    factory<GetCurrentOffer> {
        GetCurrentOffer { throw NotImplementedError("GetCurrentOffer is not implemented yet") }
    }
}
