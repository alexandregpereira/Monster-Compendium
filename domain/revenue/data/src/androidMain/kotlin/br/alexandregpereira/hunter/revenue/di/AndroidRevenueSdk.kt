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

import android.app.Application
import br.alexandregpereira.hunter.analytics.BuildConfig
import br.alexandregpereira.hunter.revenue.RevenueSdk
import br.alexandregpereira.hunter.revenue.RevenueSdkException
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.getCustomerInfoWith
import kotlinx.coroutines.suspendCancellableCoroutine

internal class AndroidRevenueSdk(
    private val app: Application,
) : RevenueSdk {

    override fun initialize() {
        if (BuildConfig.DEBUG) {
            Purchases.logLevel = LogLevel.DEBUG
        }
        val configuration = PurchasesConfiguration.Builder(
            context = app,
            apiKey = "<revenuecat_project_google_api_key>",
        ).build()
        Purchases.configure(configuration)
    }

    override suspend fun isPremiumEnabled(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val purchases = try {
                Purchases.sharedInstance
            } catch (cause: Throwable) {
                val exception = RevenueSdkException.FailToInitializeRevenueSdk(cause)
                continuation.resumeWith(Result.failure(exception))
                return@suspendCancellableCoroutine
            }
            purchases.getCustomerInfoWith(
                onError = { error ->
                    val exception = RevenueSdkException.FailToVerifyPremium(
                        code = error.code.toString(),
                        message = "${error.message} " +
                                "underlyingErrorMessage=${error.underlyingErrorMessage}",
                    )
                    continuation.resumeWith(Result.failure(exception))
                },
                onSuccess = { customerInfo ->
                    val isPremium = customerInfo.entitlements["pro"]?.isActive == true
                    continuation.resumeWith(Result.success(isPremium))
                }
            )
        }
    }
}
